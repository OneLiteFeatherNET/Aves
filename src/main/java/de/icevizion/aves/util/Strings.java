package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S3008", "java:S1854", "java:S125"})
public final class Strings {

    private static final char SPACE = ' ';

    public static NamedTextColor FILLED_HEART = NamedTextColor.RED;
    public static NamedTextColor EMPTY_HEARTS = NamedTextColor.GRAY;
    public static final String UTF_8_HEART = "♥";

    private Strings() {}

    /**
     * Creates a progress bar for the given values.
     * @param current The current amount
     * @param max The maximum amount
     * @param totalBars The maximum bars to display
     * @param symbol The symbol to display
     * @param completedColor The color for the completed part
     * @param notCompletedColor The color for the not completed part
     * @return The progressbar as string
     */
    @SuppressWarnings("java:S1481")
    @Contract(pure = true)
    public static String getProgressBar(int current,
                                        int max,
                                        int totalBars,
                                        char symbol,
                                        NamedTextColor completedColor,
                                        NamedTextColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);
       /* return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);*/
        return "";
    }

    /**
     * Converts the life of a player into a string. Full hearts are displayed in red and empty hearts in grey.
     * @param paramHealth The health of a player
     * @param remainingHearth The color for the remaining hearths
     * @param goneHearth The color for the hearth which are gone
     * @return The converted health as string
     */
    @NotNull
    public static Component getHealthString(double paramHealth,
                                            @NotNull NamedTextColor remainingHearth,
                                            @NotNull NamedTextColor goneHearth) {
        int health = (int) Math.round(paramHealth);
        health /= 2;
        int healthAway = 10 - health;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < health; i++) {
            builder.append(remainingHearth).append(UTF_8_HEART);
        }
        for (int i = 0; i < healthAway; i++) {
            builder.append(goneHearth).append(UTF_8_HEART);
        }
        return LegacyComponentSerializer.legacySection().deserialize(builder.toString());
    }

    /**
     * Converts the life of a player into a string. Full hearts are displayed in red and empty hearts in grey.
     * @param paramHealth The health of a player
     * @return The converted health as string
     */
    @NotNull
    public static Component getHealthString(double paramHealth) {
        return getHealthString(paramHealth, FILLED_HEART, EMPTY_HEARTS);
    }

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