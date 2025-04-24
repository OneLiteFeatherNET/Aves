package net.theevilreaper.aves.util.exception;

import net.theevilreaper.aves.util.functional.ThrowingFunction;
import org.jetbrains.annotations.NotNull;

/**
 * The exception will be thrown by the {@link ThrowingFunction}.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class ThrowingException extends RuntimeException {

    /**
     * Creates a new instance from the exception with a given message.
     * @param message the message for the exception
     */
    public ThrowingException(@NotNull String message) {
        super(message);
    }

    /**
     * Creates a new instance from the exception with the given values.
     * @param message the message for the exception
     * @param cause the cause as {@link Throwable}
     */
    public ThrowingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance from the exception with a {@link Throwable}.
     * @param cause the cause as {@link Throwable}
     */
    public ThrowingException(Throwable cause) {
        super(cause);
    }
}
