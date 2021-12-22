package de.arthurpicht.meta.cli.target;

public class TargetException extends Exception {

    public TargetException() {
    }

    public TargetException(String message) {
        super(message);
    }

    public TargetException(String message, Throwable cause) {
        super(message, cause);
    }

    public TargetException(Throwable cause) {
        super(cause);
    }

    public TargetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
