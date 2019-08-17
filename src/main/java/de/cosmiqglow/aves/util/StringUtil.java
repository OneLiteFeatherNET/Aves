package de.cosmiqglow.aves.util;

import org.bukkit.ChatColor;

public class StringUtil {

    public static String getProgressBar(double percent, int totalBars, String symbol, ChatColor finished, ChatColor unfinished) {
        if (percent > 1 || percent < 0) return "";
        int progressBars = (int) (totalBars * percent);
        int leftOverBars = (totalBars - progressBars);
        StringBuilder sb = new StringBuilder();
        sb.append(finished);
        for (int i = 0; i < progressBars; i++) {
            sb.append(symbol);
        }
        sb.append(unfinished);
        for (int i = 0; i < leftOverBars; i++) {
            sb.append(symbol);
        }
        return sb.toString();
    }

    public static String repeat(String text, int count) {
        if (count <= 1) {
            return count == 0 ? "" : text;
        }
        int length = text.length();
        long longSize = length * count;
        int size = (int)longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
        }
        char[] array = new char[size];
        int n;
        text.getChars(0, length, array, 0);
        for (n = length; n < size - n; n <<= 1) {
            System.arraycopy(array, 0, array, n, n);
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }

    public static String getHealthString(double paramHealth) {
        int health = (int) Math.round(paramHealth);
        health /= 2;
        int healthAway = 10 - health;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < health; i++) {
            builder.append(ChatColor.RED).append("♥");
        }
        for (int i = 0; i < healthAway; i++) {
            builder.append(ChatColor.GRAY).append("♥");
        }
        return builder.toString();
    }

    public static String centerText(String text, int linelength) {
        StringBuilder builder = new StringBuilder(text);
        char space = ' ';
        int distance = (linelength - text.length()) / 2;
        for (int i = 0; i < distance; i++) {
            builder.insert(0, space);
            builder.append(space);
        }
        return builder.toString();
    }

    public static String toRepeat(String toRepeat, int count) {
        StringBuilder builder = new StringBuilder();
        while (count > 0) {
            builder.append(toRepeat);
            --count;
        }
        return builder.toString();
    }
}