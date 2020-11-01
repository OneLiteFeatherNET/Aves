package de.icevizion.aves.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
     * Rotates the given vector around the axis X
     * @param v a vector
     * @param angle angle from the x axis
     * @return rotated vector
     */

    public static Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() - sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    /**
     * Rotates the given vector around the axis Y
     * @param v a vector
     * @param angle angle from the x axis
     * @return rotated vector
     */

    public static Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    /**
     * Rotates the given vector around the axis Z
     * @param v a vector
     * @param angle angle from the z axis
     * @return rotated vector
     */

    public static Vector rotateAroundAxisZ(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    /**
     * Rotate the given vector around the axis x, y and z
     * @param vector a vector
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @return rotated vector
     */

    public static Vector rotateVector(Vector vector, double x, double y, double z) {
        rotateAroundAxisX(vector, x);
        rotateAroundAxisY(vector, y);
        rotateAroundAxisZ(vector, z);
        return vector;
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

    public static Vector rotateAboutX(Vector vect, double a) {
        double Y = Math.cos(a) * vect.getY() - Math.sin(a) * vect.getZ();
        double Z = Math.sin(a) * vect.getY() + Math.cos(a) * vect.getZ();
        return vect.setY(Y).setZ(Z);
    }

    public static Vector rotateAboutY(Vector vect, double b) {
        double X = Math.cos(b) * vect.getX() + Math.sin(b) * vect.getZ();
        double Z = -Math.sin(b) * vect.getX() + Math.cos(b) *vect.getZ();
        return vect.setX(X).setZ(Z);
    }

    public static Vector rotateAboutZ(Vector vect, double c) {
        double X = Math.cos(c) * vect.getX() - Math.sin(c) * vect.getY();
        double Y = Math.sin(c) * vect.getX() + Math.cos(c) * vect.getY();
        return vect.setX(X).setY(Y);
    }

    public static Vector getRandomVector() {
        double x = random.nextDouble() * 2.0D - 1.0D;
        double y = random.nextDouble() * 2.0D - 1.0D;
        double z = random.nextDouble() * 2.0D - 1.0D;
        return new Vector(x, y, z).normalize();
    }

    public static Vector getBackVector(Location location) {
        float newZ = (float)(location.getZ() + 1.0D * Math.sin(Math.toRadians(location.getYaw() + 90.0F)));
        float newX = (float)(location.getX() + 1.0D * Math.cos(Math.toRadians(location.getYaw() + 90.0F)));
        return new Vector(newX - location.getX(), 0.0D, newZ - location.getZ());
    }

    public static Vector getRandomCircleVector() {
        double rnd = random.nextDouble() * 2.0D * 3.141592653589793D;
        double x = Math.cos(rnd);
        double z = Math.sin(rnd);
        return new Vector(x, 0.0D, z);
    }

    public static Vector getBlockToVector(Block block) {
        return block.getLocation().toVector();
    }

    public static double angleToXAxis(Vector vector) {
        return Math.atan2(vector.getX(), vector.getY());
    }
}