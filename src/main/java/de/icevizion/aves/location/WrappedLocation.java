package de.icevizion.aves.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * The class is a wrapper for the location class from bukkit.
 */

public class WrappedLocation {

    private String world;
    private double x;
    private double y;
    private double z;

    private float yaw;
    private float pitch;

    private transient Location location;

    /**
     * Creates a new object from the wrapper with the given values.
     * @param world The world name as string
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     */

    public WrappedLocation(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    /**
     * Creates a new object from the wrapper with the given values.
     * @param world The world name as string
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @param yaw The yaw value
     * @param pitch The pitch value
     */

    public WrappedLocation(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Give the wrapper a world name.
     * @param world The world name to set
     */

    public void setWorld(String world) {
        this.world = world;
    }

    /**
     * Set the current x coordinate.
     * @param x The x coordinate to set
     */

    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set the current z coordinate.
     * @param y The y coordinate to set
     */

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set the current the z coordinate.
     * @param z The z coordinate to set
     */

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Set the current yaw value.
     * @param yaw The yaw value to set
     */

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * Set the current pitch value.
     * @param pitch The pitch value to set
     */

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Location toLocation() {
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null) {
            throw new IllegalArgumentException("The world can not be null");
        }

        if (location == null) {
            location = new Location(bukkitWorld, x,y,z);
            location.setYaw(yaw);
            location.setPitch(pitch);
        }
        return location;
    }

    /**
     * Returns the given wrapped location as vector.
     * @return The location as vector
     */

    public Vector toVector() {
        return new Vector(x,y,z);
    }
}
