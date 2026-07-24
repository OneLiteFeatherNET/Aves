package net.theevilreaper.aves.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class providing mathematical vector operations and generation functions
 * tailored for Minecraft server development using Minestom's {@link Vec} and {@link Pos}.
 * All random vector computations leverage non-blocking, thread-safe {@link ThreadLocalRandom} instances
 * for high-throughput performance across server tick workers.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 1.0.0
 */
public final class Vectors {

    private Vectors() {
    }

    /**
     * Returns a vector which contains random values.
     *
     * @return a new vector with random values
     */
    public static Vec getRandomVector() {
        var random = ThreadLocalRandom.current();
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
    public static Vec getBackVector(Pos location) {
        float newZ = (float) (location.z() + Math.sin(Math.toRadians(location.yaw() + 90.0F)));
        float newX = (float) (location.x() + Math.cos(Math.toRadians(location.yaw() + 90.0F)));
        return new Vec(newX - location.x(), 0.0D, newZ - location.z());
    }

    /**
     * Returns a random vector which comes with values from a circle.
     *
     * @return a new vector with random values
     */
    public static Vec getRandomCircleVector() {
        double rnd = ThreadLocalRandom.current().nextDouble() * 2.0D * Math.PI;
        double x = Math.cos(rnd);
        double z = Math.sin(rnd);
        return new Vec(x, 0.0D, z);
    }

    /**
     * Calculates the angle between a 2D vector and the positive x-axis in a Cartesian coordinate system.
     * The angle is measured in radians and ranges from -π to π radians.
     *
     * @param vector the vector for which to calculate the angle
     * @return the angle between the vector and the positive x-axis in radians
     */
    @Contract(pure = true)
    public static double angleToXAxis(Vec vector) {
        return Math.atan2(vector.y(), vector.x());
    }
}
