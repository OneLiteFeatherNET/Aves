package de.icevizion.aves.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Objects;

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
    private transient Vector vector;

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

        if (location != null) {
            location.setX(x);
        }

        if (vector != null) {
            vector.setX(x);
        }
    }

    /**
     * Set the current z coordinate.
     * @param y The y coordinate to set
     */

    public void setY(double y) {
        this.y = y;

        if (location != null) {
            location.setY(y);
        }

        if (vector != null) {
            vector.setY(y);
        }
    }

    /**
     * Set the current the z coordinate.
     * @param z The z coordinate to set
     */

    public void setZ(double z) {
        this.z = z;

        if (location != null) {
            location.setZ(z);
        }

        if (vector != null) {
            vector.setZ(z);
        }
    }

    /**
     * Set the current yaw value.
     * @param yaw The yaw value to set
     */

    public void setYaw(float yaw) {
        this.yaw = yaw;

        if (location != null) {
            location.setYaw(yaw);
        }
    }

    /**
     * Set the current pitch value.
     * @param pitch The pitch value to set
     */

    public void setPitch(float pitch) {
        this.pitch = pitch;

        if (location != null) {
            location.setPitch(pitch);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrappedLocation that = (WrappedLocation) o;
        return this.x == that.x && this.y == that.y &&
                this.z == that.z && this.pitch == that.pitch && this.yaw == that.yaw && this.world.equals(that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z, yaw, pitch);
    }

    /**
     * Returns the current world.
     * @return the world
     */

    public String getWorld() {
        return world;
    }

    /**
     * Returns the x value.
     * @return x value
     */

    public double getX() {
        return x;
    }

    /**
     * Returns the y value.
     * @return y value
     */

    public double getY() {
        return y;
    }

    /**
     * Returns the z value.
     * @return z value
     */

    public double getZ() {
        return z;
    }

    /**
     * Returns the pitch value.
     * @return pitch value
     */

    public float getPitch() {
        return pitch;
    }

    /**
     * Returns the yaw value.
     * @return yaw value
     */

    public float getYaw() {
        return yaw;
    }

    /**
     * Converts the wrapped location to a bukkit location.
     * @return The bukkit location
     */

    public Location toLocation() {
        if (location == null) {
            World bukkitWorld = Bukkit.getWorld(world);

            if (bukkitWorld == null) {
                throw new IllegalArgumentException("The world can not be null");
            }

            location = new Location(bukkitWorld, x, y, z);
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
        if (vector == null) {
            vector = new Vector(x, y, z);
        }

        return vector;
    }
}
