package net.theevilreaper.aves.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

/**
 * The class contains some usefully method to work with vectors.
 *
 * @author theEvilReaper
 * @version 1.0.1
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public final class Vectors {

    private static final SecureRandom random = new SecureRandom();

    private Vectors() {
    }

    /**
     * Returns a vector which contains random values.
     *
     * @return a new vector with random values
     */
    @Contract(pure = true)
    public static @NotNull Vec getRandomVector() {
        double x = random.nextDouble() * 2.0D - 1.0D;
        double y = random.nextDouble() * 2.0D - 1.0D;
        double z = random.nextDouble() * 2.0D - 1.0D;
        return new Vec(x, y, z).normalize();
    }

    /**
     * Returns the backward vector from a given location.
     *
     * @param location The location to get the forward vector
     * @return The forward vector
     */
    @Contract("_ -> new")
    public static @NotNull Vec getBackVector(@NotNull Pos location) {
        float newZ = (float) (location.z() + Math.sin(Math.toRadians(location.yaw() + 90.0F)));
        float newX = (float) (location.x() + Math.cos(Math.toRadians(location.yaw() + 90.0F)));
        return new Vec(newX - location.x(), 0.0D, newZ - location.z());
    }

    /**
     * Returns a random vector which comes with values from a circle.
     *
     * @return a new vector with random values
     */
    @Contract(pure = true)
    public static @NotNull Vec getRandomCircleVector() {
        double rnd = random.nextDouble() * 2.0D * Math.PI;
        double x = Math.cos(rnd);
        double z = Math.sin(rnd);
        return new Vec(x, 0.0D, z);
    }

    /**
     * Calculates the angle between a 2D vector and the positive x-axis in a Cartesian coordinate system.
     * The angle is measured in radians and ranges from -π to π radians.
     *
     * @param vector the  vector for which to calculate the angle
     * @return the angle between the vector and the positive x-axis in radians
     */
    public static double angleToXAxis(@NotNull Vec vector) {
        return Math.atan2(vector.x(), vector.y());
    }
}
