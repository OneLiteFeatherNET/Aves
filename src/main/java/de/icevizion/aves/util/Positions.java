package de.icevizion.aves.util;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Includes some helpful methods for the {@link Pos} and {@link net.minestom.server.coordinate.Vec} from minestom.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class Positions {

    /**
     * Calculates the yaw value from two given double values.
     * @param dX The x value
     * @param dZ The y value
     * @return the calculated yaw value
     */
    public static float getYaw(double dX, double dZ) {
        return (float) Math.atan2(dX, dZ);
    }

    /**
     * Center a given pos in the 3D dimension.
     * @param pos The pos to center
     * @return the centered pos
     */
    @Contract(pure = true)
    public static @NotNull Pos centerPos3D(@NotNull Pos pos) {
        return pos.add(0.5, 0.5, 0.5);
    }
}
