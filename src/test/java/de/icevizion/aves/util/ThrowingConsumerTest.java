package de.icevizion.aves.util;

import de.icevizion.aves.util.functional.ThrowingFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/
public class ThrowingConsumerTest {

    @Test()
    void testThrowConsumer() {
        NullPointerException pointerException = assertThrows(
                NullPointerException.class,
                () -> ((ThrowingFunction<Object, Object>) o -> {
                    throw new NullPointerException("NOPE");
                }).acceptThrows(null)
        );
        assertEquals("NOPE", pointerException.getMessage());
    }
}
