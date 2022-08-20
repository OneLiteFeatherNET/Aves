package de.icevizion.aves.inventory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SizeCheckerTest {

    SizeChecker sizeChecker;

    @BeforeAll
    void init() {
        this.sizeChecker = new SizeCheckerImpl();
    }

    @Test
    void testValidInventorySize() {
        assertDoesNotThrow(() -> this.sizeChecker.checkInventorySize(9));
    }

    @Test
    void testCheckSize() {
        assertThrows(IllegalArgumentException.class,
                () -> this.sizeChecker.checkSize(10, 3),
                "The given value 10 is higher than the maximum 3");
    }

    private static class SizeCheckerImpl implements SizeChecker { }

}