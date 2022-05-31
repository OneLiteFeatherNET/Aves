package de.icevizion.aves.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    @Test
    void getTimeString() {
        var expectedFormat = "00:00";
        assertEquals(expectedFormat, Strings.getTimeString(0));
    }
}