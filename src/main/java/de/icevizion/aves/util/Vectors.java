package de.icevizion.aves.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Random;

public class Vectors {

    private final static Random random = new Random();

    /**
     * Inverse the direction from a player.
     * @param player The player from which the direction should be inverted
     * @return The inverted direction
     */

    public static Vector inversePlayerDirection(Player player) {
        return player.getLocation().getDirection().multiply(-1);
    }

    /**
     * Rotates the given vector around the yaw and pitch value
     * @param vector a vector
     * @param location a location
     * @return rotated vector
     */

    public static Vector rotateVector(Vector vector, Location location) {
        return rotateVector(vector, location.getYaw(), location.getPitch());
    }

    public static Vector rotateVector(Vector v, float yawDegrees, float pitchDegrees) {
        double yaw = Math.toRadians(-1 * (yawDegrees + 90));
        double pitch = Math.toRadians(-pitchDegrees);

        double cosYaw = Math.cos(yaw);
        double cosPitch = Math.cos(pitch);
        double sinYaw = Math.sin(yaw);
        double sinPitch = Math.sin(pitch);

        double initialX, initialY, initialZ;
        double x, y, z;

        initialX = v.getX();
        initialY = v.getY();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        initialZ = v.getZ();
        initialX = x;
        z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;

        return new Vector(x, y, z);
    }

    public static Vector rotateFunction(Vector v, Location loc) {
        double yawR = loc.getYaw() / 180.0 * Math.PI;
        double pitchR = loc.getPitch() / 180.0 * Math.PI;
        v = rotateAboutX(v, pitchR);
        v = rotateAboutY(v, -yawR);
        return v;
    }

    public static Vector rotateAboutX(Vector vector, double a) {
        double Y = Math.cos(a) * vector.getY() - Math.sin(a) * vector.getZ();
        double Z = Math.sin(a) * vector.getY() + Math.cos(a) * vector.getZ();
        return vector.setY(Y).setZ(Z);
    }

    public static Vector rotateAboutY(Vector vector, double b) {
        double X = Math.cos(b) * vector.getX() + Math.sin(b) * vector.getZ();
        double Z = -Math.sin(b) * vector.getX() + Math.cos(b) *vector.getZ();
        return vector.setX(X).setZ(Z);
    }

    public static Vector rotateAboutZ(Vector vector, double c) {
        double X = Math.cos(c) * vector.getX() - Math.sin(c) * vector.getY();
        double Y = Math.sin(c) * vector.getX() + Math.cos(c) * vector.getY();
        return vector.setX(X).setY(Y);
    }

    public static Vector getRandomVector() {
        double x = random.nextDouble() * 2.0D - 1.0D;
        double y = random.nextDouble() * 2.0D - 1.0D;
        double z = random.nextDouble() * 2.0D - 1.0D;
        return new Vector(x, y, z).normalize();
    }

    public static Vector getBackVector(Location location) {
        float newZ = (float)(location.getZ() + Math.sin(Math.toRadians(location.getYaw() + 90.0F)));
        float newX = (float)(location.getX() + Math.cos(Math.toRadians(location.getYaw() + 90.0F)));
        return new Vector(newX - location.getX(), 0.0D, newZ - location.getZ());
    }

    public static Vector getRandomCircleVector() {
        double rnd = random.nextDouble() * 2.0D * 3.141592653589793D;
        double x = Math.cos(rnd);
        double z = Math.sin(rnd);
        return new Vector(x, 0.0D, z);
    }

    public static Vector ordinalEdge(int ordinal, BoundingBox bb) {
        switch (ordinal) {
            case 0:
                return new Vector(bb.getMinX(), bb.getMinY(), bb.getMinZ());
            case 1:
                return new Vector(bb.getMinX(), bb.getMinY(), bb.getMaxZ());
            case 2:
                return new Vector(bb.getMaxX(), bb.getMinY(), bb.getMaxZ());
            case 3:
                return new Vector(bb.getMaxX(), bb.getMinY(), bb.getMinZ());
            case 4:
                return new Vector(bb.getMinX(), bb.getMaxY(), bb.getMinZ());
            case 5:
                return new Vector(bb.getMinX(), bb.getMaxY(), bb.getMaxZ());
            case 6:
                return new Vector(bb.getMaxX(), bb.getMaxY(), bb.getMaxZ());
            case 7:
                return new Vector(bb.getMaxX(), bb.getMaxY(), bb.getMinZ());
            default:
                throw new ArrayIndexOutOfBoundsException(ordinal + " not in 0..7");
        }
    }

    public static Vector getBlockToVector(Block block) {
        return block.getLocation().toVector();
    }

    public static double angleToXAxis(Vector vector) {
        return Math.atan2(vector.getX(), vector.getY());
    }
}