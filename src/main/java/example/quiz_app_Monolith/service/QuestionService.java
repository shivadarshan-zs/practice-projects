package example.quiz_app_Monolith.service;

import example.quiz_app_Monolith.dao.QuestionDao;
import example.quiz_app_Monolith.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    QuestionDao questionDao;

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
}
