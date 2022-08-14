package de.icevizion.aves.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

/**
 * The class contains some usefully method to work with vectors.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public class Vectors {

    private static final SecureRandom random = new SecureRandom();

    private Vectors() {}

    @NotNull
    public static Vec getRandomVector() {
        double x = random.nextDouble() * 2.0D - 1.0D;
        double y = random.nextDouble() * 2.0D - 1.0D;
        double z = random.nextDouble() * 2.0D - 1.0D;
        return new Vec(x, y, z).normalize();
    }

    @Contract("_ -> new")
    @NotNull
    public static Vec getBackVector(@NotNull Pos location) {
        float newZ = (float)(location.z() + Math.sin(Math.toRadians(location.yaw() + 90.0F)));
        float newX = (float)(location.x() + Math.cos(Math.toRadians(location.yaw() + 90.0F)));
        return new Vec(newX - location.x(), 0.0D, newZ - location.z());
    }

    @NotNull
    public static Vec getRandomCircleVector() {
        double rnd = random.nextDouble() * 2.0D * Math.PI;
        double x = Math.cos(rnd);
        double z = Math.sin(rnd);
        return new Vec(x, 0.0D, z);
    }

    public static double angleToXAxis(@NotNull Vec vector) {
        return Math.atan2(vector.x(), vector.y());
    }
}