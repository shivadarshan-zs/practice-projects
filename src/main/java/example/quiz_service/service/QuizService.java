package com.example.quiz_service.service;

import com.example.quiz_service.dao.QuizDao;
import com.example.quiz_service.feign.QuizInterface;
import com.example.quiz_service.model.QuestionWrapper;
import com.example.quiz_service.model.Quiz;
import com.example.quiz_service.model.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    @CircuitBreaker(name = "questionService", fallbackMethod = "createQuizFallback")
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questionIds = quizInterface.getQuestionsForQuiz(category, numQ).getBody();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questionIds);

        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<String> createQuizFallback(String category, int numQ, String title, Throwable ex) {
        logger.error("Question service DOWN while creating quiz", ex);
        return new ResponseEntity<>("Service unavailable. Try later.", HttpStatus.SERVICE_UNAVAILABLE);
    }


    @CircuitBreaker(name = "questionService", fallbackMethod = "getQuizQuestionsFallback")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {

        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();

        List<QuestionWrapper> questions = quizInterface.getQuestionsFromIds(questionIds).getBody();

        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestionsFallback(Integer id, Throwable ex) {
        logger.error("Question service DOWN while fetching quiz questions", ex);
        return new ResponseEntity<>(List.of(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @CircuitBreaker(name = "questionService", fallbackMethod = "calculateResultFallback")
    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        Integer score = quizInterface.calculateScore(responses).getBody();

        return new ResponseEntity<>(score, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResultFallback(Integer id, List<Response> responses, Throwable ex) {
        logger.error("Question service DOWN while calculating score", ex);
        return new ResponseEntity<>(0, HttpStatus.SERVICE_UNAVAILABLE);
    }
}