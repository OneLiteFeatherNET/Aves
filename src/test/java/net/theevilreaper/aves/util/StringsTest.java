package net.theevilreaper.aves.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    private static final Stream<Arguments> TIME_ARGUMENTS = Stream.of(
            Arguments.of("00:00", Strings.getTimeString(TimeFormat.MM_SS, 0)),
            Arguments.of("01:00", Strings.getTimeString(TimeFormat.MM_SS, 60)),
            Arguments.of("70:00", Strings.getTimeString(TimeFormat.MM_SS, 4200)),
            Arguments.of("70:55", Strings.getTimeString(TimeFormat.MM_SS, 4255)),
            Arguments.of("01:00:00", Strings.getTimeString(TimeFormat.HH_MM_SS, 3600)),
            Arguments.of("00:59:59", Strings.getTimeString(TimeFormat.HH_MM_SS, 3599)),
            Arguments.of("01:01:39", Strings.getTimeString(TimeFormat.HH_MM_SS, 3699)),
            Arguments.of("00:00:00", Strings.getTimeString(TimeFormat.HH_MM_SS, -23))
    );

    private static Stream<Arguments> getArguments() {
        return TIME_ARGUMENTS;
    }

    private static Stream<Arguments> getLineArguments() {
        return LINE_ARGUMENTS;
    }

    private static final Stream<Arguments> LINE_ARGUMENTS = Stream.of(
            Arguments.of(assertThrows(
                            IllegalArgumentException.class,
                            () -> Strings.centerText("", 10)).getMessage(),
                    "The text can not be empty"
            ),
            Arguments.of(
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> Strings.centerText("Hallo", 2)).getMessage(),
                    "The length of the line must be greater than the text length"
            ),
            Arguments.of(Strings.centerText("Hallo", 8), " Hallo "),
            Arguments.of(Strings.centerText("Hallo", 10), "  Hallo  ")
    );

    @ParameterizedTest
    @MethodSource("getArguments")
    void testGetTimeString(String expected, String value) {
        assertEquals(expected, value);
    }

    @ParameterizedTest
    @MethodSource("getLineArguments")
    void testCenterString(String expected, String value) {
        assertEquals(expected, value);
    }

    @Test
    void testFalseCenterText() {
        var centeredText = Strings.centerText("Hallo", 9);
        assertNotEquals(" Hallo ", centeredText);
    }
}