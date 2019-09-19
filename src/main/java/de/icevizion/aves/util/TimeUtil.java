package de.icevizion.aves.util;

import com.google.common.base.Joiner;

public class TimeUtil {

    /**
     * Convert a time value into the hh:mm format
     * @param time The time who should be converted
     * @return The converted time
     */

    public static String getTimeString(final int time) {
        if (time <= 0) {
            return "00:00";
        }
        int minutes = time / 60;
        int seconds = time % 60;
        return Joiner.on(":").join((minutes < 10) ? "0" + minutes : minutes, (seconds < 10) ? "0" + seconds : seconds);
    }
}