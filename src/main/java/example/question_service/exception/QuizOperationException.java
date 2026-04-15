package example.question_service.exception;

public class QuizOperationException extends RuntimeException {
    public QuizOperationException(String message) {
        super(message);
    }

    public QuizOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

