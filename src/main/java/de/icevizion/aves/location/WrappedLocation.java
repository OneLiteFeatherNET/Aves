package de.icevizion.aves.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The class is a wrapper for the location class from bukkit.
 * @author theEvilReaper
 * @since 1.0.5
 * @version 1.3.0
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

    //Constructor for jsoniter serialization
    public WrappedLocation() {}

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
     * Create a new object from that class with the given values.
     * @param location The location to wrap
     */

    private WrappedLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.location = location;
    }

    /**
     * Converts a given location to a {@link WrappedLocation} with all relevant values.
     * @param location The location to convert
     * @return the transformed location as {@link WrappedLocation}
     */

    public static WrappedLocation of(Location location) {
        return new WrappedLocation(location);
    }

    /**
     * Give the wrapper a world name.
     * @param world The world name to set
     */

    public void setWorldName(String world) {
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

    /**
     * Adds the specified values to the location.
     * If the {@link Location} is not null, it will also be updated
     * If the {@link Vector} is not null, it will also be updated
     * @param x The x value to add
     * @param y The y value to add
     * @param z The z value to add
     */

    public WrappedLocation add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;

        if (location != null) {
            location.add(x, y, z);
        }

        if (vector != null) {
            vector.add(new Vector(x ,y ,z));
        }

        return this;
    }

    /**
     * Subtracts the specified values from the location.
     * If the {@link Location} is not null, it will also be updated
     * If the {@link Vector} is not null, it will also be updated
     * @param x The x value to subtract
     * @param y The y value to subtract
     * @param z The z value to subtract
     */

    public WrappedLocation subtract(int x, int y, int z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;

        if (location != null) {
            location.subtract(x, y, z);
        }

        if (vector != null) {
            vector.subtract(new Vector(x ,y ,z));
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Location) {
            Location location = (Location) o;
            return (this.world.equals(location.getWorld().getName()) &&
                    this.x == location.getX() && this.y == location.getY() &&
                    this.z == location.getZ() && this.yaw == location.getYaw() && this.pitch == location.getPitch());
        } else {
            WrappedLocation that = (WrappedLocation) o;
            return this.x == that.x && this.y == that.y &&
                    this.z == that.z && this.pitch == that.pitch &&
                    this.yaw == that.yaw && this.world.equals(that.world);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z, yaw, pitch);
    }

    /**
     * Returns the current world.
     * @return the world
     */

    public String getWorldName() {
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
            World bukkitWorld = Bukkit.getWorld(this.world);

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

    /**
     * Returns the world from the {@link Location}.
     * @return The underlying world from the bukkit location
     */

    @Nullable
    public World getBukkitWorld() {
        return location == null ? null : location.getWorld();
    }
}