package net.theevilreaper.aves.inventory.exception;

import org.jetbrains.annotations.NotNull;

/**
 * The {@link ListenerStateException} is a custom exception which is thrown when a listener is in an invalid state.
 * Its former used in the in inventory listener handling to indicate that the listener is not in a valid state to process an event.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.12
 */
public final class ListenerStateException extends IllegalStateException {

    /**
     * Creates a new instance of the {@link ListenerStateException} with the given message.
     *
     * @param message the message for the exception
     */
    public ListenerStateException(@NotNull String message) {
        super(message);
    }

    /**
     * Creates a new instance of the {@link ListenerStateException} with the given message and cause.
     *
     * @param message the message for the exception
     * @param cause   the cause of the exception
     */
    public ListenerStateException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of the {@link ListenerStateException} with the given cause.
     *
     * @param cause the cause of the exception
     */
    public ListenerStateException(@NotNull Throwable cause) {
        super(cause);
    }
}
