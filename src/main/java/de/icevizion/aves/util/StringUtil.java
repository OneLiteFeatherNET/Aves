package de.icevizion.aves.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class StringUtil {

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

    public static String getProgressBar(int current, int max, int totalBars, char symbol, NamedTextColor completedColor,
                                        NamedTextColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);
        return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }

    /**
     * Converts the life of a player into a string. Full hearts are displayed in red and empty hearts in grey.
     * @param paramHealth The health of a player
     * @return The converted health as string
     */

    public static Component getHealthString(double paramHealth) {
        int health = (int) Math.round(paramHealth);
        health /= 2;
        int healthAway = 10 - health;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < health; i++) {
            builder.append(NamedTextColor.RED).append("♥");
        }
        for (int i = 0; i < healthAway; i++) {
            builder.append(NamedTextColor.GRAY).append("♥");
        }
        return LegacyComponentSerializer.legacySection().deserialize(builder.toString());
    }

    /**
     * Centers a given text with a given length of a line.
     * @param text The text to center
     * @param lineLength The length of a line
     * @return The centered text
     */

    public static String centerText(String text, int lineLength) {
        StringBuilder builder = new StringBuilder(text);
        char space = ' ';
        int distance = (lineLength - text.length()) / 2;
        for (int i = 0; i < distance; i++) {
            builder.insert(0, space);
            builder.append(space);
        }
        return builder.toString();
    }

    /**
     * Convert a time value into the hh:mm format.
     * @param time The time who should be converted
     * @return The converted time
     */

    public static String getTimeString(int time) {
        if (time <= 0) {
            return "00:00";
        }
        int minutes = time / 60;
        int seconds = time % 60;
        return Joiner.on(":").join((minutes < 10) ? "0" + minutes : minutes, (seconds < 10) ? "0" + seconds : seconds);
    }

    public static String trim(String text, int length) {
        return text.length() > length ? text.substring(0, length) : text;
    }
}