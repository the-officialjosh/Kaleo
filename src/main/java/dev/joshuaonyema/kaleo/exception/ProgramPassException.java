package dev.joshuaonyema.kaleo.exception;


public class ProgramPassException extends RuntimeException{


    public ProgramPassException() {
    }

    public ProgramPassException(String message) {
        super(message);
    }

    public ProgramPassException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProgramPassException(Throwable cause) {
        super(cause);
    }

    public ProgramPassException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
