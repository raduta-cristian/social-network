package org.example.social_network.domain.validators;

public class ValidationException extends RuntimeException {
    public ValidationException() {
    }

    public ValidationException(String message) {
        super("ValidationException: " + message);
    }

    public ValidationException(String message, Throwable cause) {
        super("ValidationException: " + message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("ValidationException: " + message, cause, enableSuppression, writableStackTrace);
    }
}
