package de.icevizion.aves.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    @Test
    void testZeroTime() {
        var expectedFormat = "00:00";
        assertEquals(expectedFormat, Strings.getTimeString(0));
    }
    @Test
    void testToHighFormat() {
        var excepted = "70:00";
        assertEquals(excepted, Strings.getTimeString(4200));
    }
}