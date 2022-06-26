package de.icevizion.aves.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Vectors {

    private final static Random random = new Random();

    public static Vec getRandomVector() {
        double x = random.nextDouble() * 2.0D - 1.0D;
        double y = random.nextDouble() * 2.0D - 1.0D;
        double z = random.nextDouble() * 2.0D - 1.0D;
        return new Vec(x, y, z).normalize();
    }

    public static Vec getBackVector(@NotNull Pos location) {
        float newZ = (float)(location.z() + Math.sin(Math.toRadians(location.yaw() + 90.0F)));
        float newX = (float)(location.x() + Math.cos(Math.toRadians(location.yaw() + 90.0F)));
        return new Vec(newX - location.x(), 0.0D, newZ - location.z());
    }

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