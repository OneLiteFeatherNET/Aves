package de.icevizion.aves.util;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class Positions {

    /**
     * Calculates the yaw value from two given double values.
     * @param dX
     * @param dZ
     * @return the calculated yaw value
     */
    public static float getYaw(double dX, double dZ) {
        return (float) Math.atan2(dZ, dX);
    }
}
