package example.question_service.service;

import com.example.question_service.dao.QuestionDao;
import com.example.question_service.model.Question;
import com.example.question_service.model.QuestionWrapper;
import com.example.question_service.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    QuestionDao questionDao;

    @Autowired
    RedisTemplate<String, QuestionWrapper> userRedisTemplate;

    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionDao.findAll();
        logger.debug("Fetched {} questions", questions.size());
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        List<Question> questions = questionDao.findByCategory(category);
        logger.debug("Fetched {} questions for category: {}", questions.size(), category);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        questionDao.save(question);
        logger.info("Saved question for category: {}", question.getCategory());
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, int numQ) {
        List<Integer> questionIds = questionDao.findRandomQuestionsByCategory(category, numQ);
        logger.debug("Fetched {} question IDs for category: {}", questionIds.size(), category);
        return new ResponseEntity<>(questionIds, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        List<Integer> missingIds = new ArrayList<>();

        for (Integer id : questionIds) {
            QuestionWrapper qw = userRedisTemplate.opsForValue().get("question:" + id);
            if (qw != null) {
                logger.info("question cache HIT for id: {}", id);
                questionWrappers.add(qw);
            } else {
                logger.info("question cache MISS for id: {}", id);
                missingIds.add(id);
            }
        }

        if (!missingIds.isEmpty()) {
            List<Question> questions = questionDao.findAllById(missingIds);
            logger.debug("Fetched {} questions for quiz from DB", questions.size());

            for (Question q : questions) {
                QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
                questionWrappers.add(qw);
                userRedisTemplate.opsForValue().set("question:" + q.getId(), qw,30, TimeUnit.SECONDS);
            }
        }
        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateScore(List<Response> responses) {
        int score = 0;
        for (Response response : responses) {
            Optional<Question> question = questionDao.findById(response.getId());
            if (response.getResponse().equals(question.get().getRightAnswer()))
                score++;
        }
        logger.info("Calculated score: {}", score);
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
