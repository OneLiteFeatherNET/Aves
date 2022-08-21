package de.icevizion.aves.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    @Test
    void testZeroTime() {
        assertEquals("00:00", Strings.getTimeString(0));
    }

    @Test
    void testTime() {
        assertEquals("01:00", Strings.getTimeString(60));
    }

    @Test
    void testToHighFormat() {
        assertEquals("70:00", Strings.getTimeString(4200));
    }

    @Test
    void testToHighFormat2() {
        assertEquals("70:55", Strings.getTimeString(4255));
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

    @Test
    void testHourString() {
        assertEquals("01:00:00", Strings.getTimeWithHours(3600));
    }

    @Test
    void testHourString2() {
        assertEquals("00:59:59", Strings.getTimeWithHours(3599));
    }

    @Test
    void testHourString3() {
        assertEquals("01:01:39", Strings.getTimeWithHours(3699));
    }

    @Test
    void testNegativTime() {
        assertEquals("00:00:00", Strings.getTimeWithHours(-23));
    }
}