package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Strings {

    private static final char SPACE = ' ';

    public static NamedTextColor FILLED_HEARTH = NamedTextColor.RED;
    public static NamedTextColor LOOSED_HEARTHS = NamedTextColor.GRAY;

    public static final String UTF_8_HEART = "♥";

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
        return getHealthString(paramHealth, FILLED_HEARTH, LOOSED_HEARTHS);
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


    @Contract(pure = true)
    public static @NotNull String getTimeWithHours(int time) {
        if (time <= 0) {
            return "00:00:00";
        }

        int minutes = time / 60;
        int hours = minutes / 60;

        minutes = minutes % 60;

        int seconds = time % 60;
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    /**
     * Convert a time value into the hh:mm format.
     * @param time The time who should be converted
     * @return The converted time
     */
    @Contract(pure = true)
    public static @NotNull String getTimeString(int time) {
        if (time <= 0) {
            return "00:00";
        }
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }
}