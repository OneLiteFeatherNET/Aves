package net.theevilreaper.aves.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

/**
 * The class contains some useful methods for string manipulation.
 * Each method has a specific context for the game Minecraft and doesn't relate to a general string manipulation utility.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
@ApiStatus.NonExtendable
public final class Strings {

    private static final String INT_FORMAT = "%02d";
    private static final int TIME_DIVIDER = 60;

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
    public static @NotNull String centerText(@NotNull String text, int lineLength) {
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
    @Contract(pure = true)
    public static @NotNull String getTimeString(@NotNull TimeFormat timeFormat, int time) {
        if (time <= 0) {
            return timeFormat.getDefaultFormat();
        }

        int minutes = time / TIME_DIVIDER;
        int seconds = time % TIME_DIVIDER;

        StringJoiner stringJoiner = new StringJoiner(":");

        if (timeFormat == TimeFormat.HH_MM_SS) {
            int hours = minutes / TIME_DIVIDER;
            minutes = minutes % TIME_DIVIDER;
            stringJoiner.add(String.format(INT_FORMAT, hours));
        }
        stringJoiner.add(String.format(INT_FORMAT, minutes));
        stringJoiner.add(String.format(INT_FORMAT, seconds));
        return stringJoiner.toString();
    }
}