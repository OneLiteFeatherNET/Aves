package net.theevilreaper.aves.util.functional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoidConsumerTest {

    @Test
    void testBasicVoidConsumer() {
        VoidConsumer voidConsumer = () -> {
            throw new RuntimeException("Call me!");
        };

        assertNotNull(voidConsumer);
        RuntimeException exception = assertThrows(RuntimeException.class, voidConsumer::apply);
        assertNotNull(exception);
        assertInstanceOf(RuntimeException.class, exception);
        assertEquals("Call me!", exception.getMessage());
    }
}
