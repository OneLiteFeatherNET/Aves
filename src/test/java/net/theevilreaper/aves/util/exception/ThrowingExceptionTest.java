package net.theevilreaper.aves.util.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowingExceptionTest {

    @Test
    void testConstructorWithString() {
        assertThrowsExactly(ThrowingException.class, this::throwWithName, "Test");
    }

    @Test
    void testConstructorWithStringAndThrowable() {
        var exception = assertThrowsExactly(ThrowingException.class, this::throwWithNameAndThrowable, "Test");
        assertSame(Exception.class, exception.getCause().getClass());
    }

    @Test
    void testConstructorWithThrowable() {
        var exception = assertThrowsExactly(ThrowingException.class, this::throwWithThrowable);
        assertSame(Throwable.class, exception.getCause().getClass());
    }

    private void throwWithName() {
        throw new ThrowingException("Test");
    }

    private void throwWithThrowable() {
        throw new ThrowingException(new Throwable());
    }

    private void throwWithNameAndThrowable() {
        throw new ThrowingException("Test", new Exception());
    }

}