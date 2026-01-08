package dev.joshuaonyema.kaleo.exception;

public class PassSoldOutException extends RuntimeException {

    public PassSoldOutException() {
    }

    public PassSoldOutException(String message) {
        super(message);
    }

    public PassSoldOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public PassSoldOutException(Throwable cause) {
        super(cause);
    }

    public PassSoldOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
