package de.icevizion.aves.util;

import org.bukkit.util.EulerAngle;

/**
 * The class contains a helper method that deals with the topic EulerAngle.
 */

public class EulerUtil {

    /**
     * Converts a given degree value to an EulerAngle.
     * @param degrees The degree value
     * @return The converted degree value as {@link EulerAngle}
     */

    public static EulerAngle degreesToEulerAngle(int degrees) {
        return radiansToEulerAngle(Math.toRadians(degrees));
    }

    /**
     * Converts a given radians value to an EulerAngle.
     * @param radians The radians value
     * @return The converted radians value as {@link EulerAngle}
     */

    public static EulerAngle radiansToEulerAngle(double radians) {
        return new EulerAngle(Math.cos(radians), 0 , Math.sin(radians));
    }
}
