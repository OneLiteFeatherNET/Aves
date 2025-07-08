package net.theevilreaper.aves.inventory.exception;

public final class ListenerStateException extends IllegalStateException {

    public ListenerStateException(String message) {
        super(message);
    }

    public ListenerStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListenerStateException(Throwable cause) {
        super(cause);
    }
}
