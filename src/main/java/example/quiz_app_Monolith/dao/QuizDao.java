package example.quiz_app_Monolith.dao;

import example.quiz_app_Monolith.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz,Integer> {
}
