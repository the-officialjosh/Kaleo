package dev.joshuaonyema.kaleo.exception;

public class CodeNotFoundException extends RuntimeException {

    public CodeNotFoundException() {
    }

    public CodeNotFoundException(String message) {
        super(message);
    }

    public CodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public CodeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
