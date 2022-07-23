package de.icevizion.aves.util;

import net.minestom.server.coordinate.Point;
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

    public static @NotNull Point normalizePoint(@NotNull Point pos) {
        var x = lengthSquaredPos(pos);

        var xHalf =  .5D * x;

        var i = Double.doubleToRawLongBits(x);
        i = 0x5fe6ec85e7de30daL - (i << 1);
        x = Double.longBitsToDouble(i);
        x *= (1.5 - xHalf * x * x);

        return pos.mul(x);
    }

    private static double lengthSquaredPos(@NotNull Point pos) {
        return Math.pow(pos.x(), 2) + Math.pow(pos.y(), 2) + Math.pow(pos.z(), 2);
    }
}
