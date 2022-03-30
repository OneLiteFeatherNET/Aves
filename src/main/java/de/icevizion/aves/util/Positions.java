package de.icevizion.aves.util;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/
public class Positions {

    public static float getYaw(double dX, double dZ) {
        return (float) Math.atan2(dZ, dX);
    }
}
