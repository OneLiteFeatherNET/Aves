package net.theevilreaper.aves.util;

import org.jetbrains.annotations.Contract;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The class contains some useful methods for string manipulation.
 * Each method has a specific context for the game Minecraft and doesn't relate to a general string manipulation utility.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
public final class Strings {

    private static final DateTimeFormatter HH_MM_SS =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final DateTimeFormatter MM_SS =
            DateTimeFormatter.ofPattern("mm:ss");

    public static final String SPACE = " ";
    public static final String UTF_8_HEART = "\u2665";

    private Strings() {
        // Utility class nothing to instantiate
    }

    /**
     * Centers a given text with a given length of a line.
     *
     * @param text       The text to center
     * @param lineLength The length of a line
     * @return The centered text
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static String centerText(String text, int lineLength) {
        text = text.trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("The text can not be empty");
        }
        if (lineLength < text.length()) {
            throw new IllegalArgumentException("The length of the line must be greater than the text length");
        }

        int totalPadding = lineLength - text.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding / 2; // This ensures equal padding

        return SPACE.repeat(leftPadding) + text + SPACE.repeat(rightPadding);
    }

    /**
     * Convert a time value into the given format from the {@link TimeFormat} entry.
     *
     * @param timeFormat the format, which should be used for the conversion
     * @param time       the time who should be converted
     * @return the converted time
     */
    @Contract(pure = true, value = "_, _ -> new")
    public static String getTimeString(TimeFormat timeFormat, int time) {
        if (time <= 0) {
            return timeFormat.getDefaultFormat();
        }

        int seconds = time % 60;
        int totalMinutes = time / 60;
        int minutes = totalMinutes % 60;
        int hours = totalMinutes / 60;

        LocalTime localTime = LocalTime.of(hours, minutes, seconds);

        return switch (timeFormat) {
            case HH_MM_SS -> localTime.format(HH_MM_SS);
            case MM_SS -> localTime.format(MM_SS);
        };
    }
}