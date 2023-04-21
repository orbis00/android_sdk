package com.wigzo.android.base;

public class ValidationException extends Throwable {

    public ValidationException() {
        throw new RuntimeException("Validation Exception");
    }

    public ValidationException(String message) {
        throw new RuntimeException(message);
    }

    public ValidationException(String message, Throwable cause) {
        throw new RuntimeException(message, cause);
    }

    public ValidationException(Throwable cause) {
        throw new RuntimeException(cause);
    }
}
