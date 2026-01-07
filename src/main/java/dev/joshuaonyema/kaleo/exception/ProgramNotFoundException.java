package dev.joshuaonyema.kaleo.exception;

public class ProgramNotFoundException extends RuntimeException {

    public ProgramNotFoundException() {
    }

    public ProgramNotFoundException(String message) {
        super(message);
    }

    public ProgramNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProgramNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProgramNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
