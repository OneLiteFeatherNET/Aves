package de.icevizion.aves.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeFormatTest {

    @Test
    void testFormat() {
        assertEquals("00:00", TimeFormat.MM_SS.getDefaultFormat());
        assertNotEquals(TimeFormat.MM_SS.getDefaultFormat(), TimeFormat.HH_MM_SS.getDefaultFormat());
        assertNotNull(TimeFormat.HH_MM_SS.getDefaultFormat());
    }
}