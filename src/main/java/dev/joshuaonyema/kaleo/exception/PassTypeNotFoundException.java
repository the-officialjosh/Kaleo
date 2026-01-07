package dev.joshuaonyema.kaleo.exception;

public class PassTypeNotFoundException extends RuntimeException {

    public PassTypeNotFoundException() {
    }

    public PassTypeNotFoundException(String message) {
        super(message);
    }

    public PassTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PassTypeNotFoundException(Throwable cause) {
        super(cause);
    }

    public PassTypeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
