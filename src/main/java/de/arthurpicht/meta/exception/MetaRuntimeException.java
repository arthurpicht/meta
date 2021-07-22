package de.arthurpicht.meta.exception;

/**
 * A fatal exception that leads to immediate break of operation for all repositories.
 */
public class MetaRuntimeException extends RuntimeException {

    public MetaRuntimeException() {
    }

    public MetaRuntimeException(String message) {
        super(message);
    }

    public MetaRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaRuntimeException(Throwable cause) {
        super(cause);
    }

    public MetaRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
