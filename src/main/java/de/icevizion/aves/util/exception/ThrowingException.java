package de.icevizion.aves.util.exception;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class ThrowingException extends RuntimeException {

    public ThrowingException(String message) {
        super(message);
    }

    public ThrowingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThrowingException(Throwable cause) {
        super(cause);
    }
}
