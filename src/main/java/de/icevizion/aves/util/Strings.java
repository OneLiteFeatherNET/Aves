package de.icevizion.aves.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public final class Strings {

    private static final char SPACE = ' ';
    public static final String UTF_8_HEART = "\u2665";

    private Strings() {}

    /**
     * Centers a given text with a given length of a line.
     * @param text The text to center
     * @param lineLength The length of a line
     * @return The centered text
     */
    @NotNull
    public static String centerText(@NotNull String text, int lineLength) {
        text = text.trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("The text can not be empty");
        }

        if (lineLength < text.length()) {
            throw new IllegalArgumentException("The length of the line must be greater than the text length");
        }

        StringBuilder builder = new StringBuilder(text);
        int distance = (lineLength - text.length()) / 2;
        for (int i = 0; i < distance; i++) {
            builder.insert(0, SPACE);
            builder.append(SPACE);
        }
        return builder.toString();
    }

    /**
     * Convert a time value into the given format from the {@link TimeFormat} entry.
     * @param time The time who should be converted
     * @return The converted time
     */
    @Contract(pure = true)
    public static @NotNull String getTimeString(@NotNull TimeFormat timeFormat, int time) {
        if (time <= 0) {
            return timeFormat.getDefaultFormat();
        }

        int minutes = time / 60;
        int seconds = time % 60;

        StringJoiner stringJoiner = new StringJoiner(":");

        if (timeFormat == TimeFormat.HH_MM_SS) {
            int hours = minutes / 60;
            minutes = minutes % 60;
            stringJoiner.add(String.format("%02d", hours));
        }
        stringJoiner.add(String.format("%02d", minutes));
        stringJoiner.add(String.format("%02d", seconds));

        return stringJoiner.toString();
    }
}