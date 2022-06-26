package de.icevizion.aves.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    @Test
    void testZeroTime() {
        assertEquals("00:00", Strings.getTimeString(0));
    }
    @Test
    void testToHighFormat() {
        assertEquals("70:00", Strings.getTimeString(4200));
    }

    @Test
    void testEmptyCenterText() {
        var exception = assertThrows(IllegalArgumentException.class, () -> Strings.centerText("", 10));
        assertEquals("The text can not be empty", exception.getMessage());
    }

    @Test
    void testLineLength() {
        var exception = assertThrows(IllegalArgumentException.class, () -> Strings.centerText("Hallo", 2));
        assertEquals("The length of the line must be greater than the text length", exception.getMessage());
    }

    @Test
    void testCenterText() {
        var centeredText = Strings.centerText("Hallo", 8);
        assertEquals(" Hallo ", centeredText);
    }

    @Test
    void testFalseCenterText() {
        var centeredText = Strings.centerText("Hallo", 9);
        assertNotEquals(" Hallo ", centeredText);
    }
}