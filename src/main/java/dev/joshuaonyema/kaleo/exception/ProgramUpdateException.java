package dev.joshuaonyema.kaleo.exception;

public class ProgramUpdateException extends RuntimeException {

    public ProgramUpdateException() {
    }

    public ProgramUpdateException(String message) {
        super(message);
    }

    public ProgramUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProgramUpdateException(Throwable cause) {
        super(cause);
    }

    public ProgramUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
