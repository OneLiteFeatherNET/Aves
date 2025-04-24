package net.theevilreaper.aves.util.functional;

import net.theevilreaper.aves.util.exception.ThrowingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowingFunctionTest {

    @Test
    void testThrowingFunction() {
        ThrowingFunction<Boolean, Boolean> function = aBoolean -> {
            if (!aBoolean) {
                throw new RuntimeException("FALSE");
            }
            return true;
        };
        assertNotNull(function);
        assertTrue(function.apply(true));
        assertThrowsExactly(
                ThrowingException.class,
                () -> function.apply(false),
                "FALSE"
        );
    }

}