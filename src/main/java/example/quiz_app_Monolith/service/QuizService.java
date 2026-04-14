package example.quiz_app_Monolith.service;

import example.quiz_app_Monolith.dao.QuestionDao;
import example.quiz_app_Monolith.dao.QuizDao;
import example.quiz_app_Monolith.exception.ResourceNotFoundException;
import example.quiz_app_Monolith.model.Question;
import example.quiz_app_Monolith.model.QuestionWrapper;
import example.quiz_app_Monolith.model.Quiz;
import example.quiz_app_Monolith.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        logger.debug("Selected {} questions for category: {}", questions.size(), category);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        logger.info("Created quiz '{}' with {} questions", title, questions.size());
        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = getQuizById(id);
        List<Question> questionsFromDB = quiz.getQuestions();
        logger.debug("Loaded {} questions for quiz id: {}", questionsFromDB.size(), id);
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        for (Question q : questionsFromDB) {
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(qw);
        }

        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);

    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Quiz quiz = getQuizById(id);
        List<Question> questions = quiz.getQuestions();
        int correct = 0;
        int i = 0;
        for (Response response : responses) {
            logger.debug("response for question id: {} is '{}', correct answer is '{}'", questions.get(i).getId(), response.getResponse(), questions.get(i).getRightAnswer());
            logger.debug("i is {} question{}", i, questions.get(i));
            if (response.getResponse().equals(questions.get(i).getRightAnswer()))
                correct++;

            i++;
        }
        logger.info("Calculated quiz result for quiz id: {}. Correct answers: {} out of {}", id, correct, responses.size());
        return new ResponseEntity<>(correct, HttpStatus.OK);
    }

    private Quiz getQuizById(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        if (quiz.isPresent()) {
            logger.debug("Found quiz with id: {}", id);
            return quiz.get();
        }
        logger.warn("Quiz not found with id: {}", id);
        throw new ResourceNotFoundException("Quiz not found with id: " + id);
    }
}
