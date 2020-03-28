package de.icevizion.aves.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * The class contains some methods that work with {@link Location}
 */
public class LocationUtil {

    /**
     * Returns the center of the block.
     * @param location The block where the center should be calculated from
     * @return The middle location of the given block
     */

    public static Location getCenter(Location location) {
        return location.add(0.5, 0, 0.5);
    }

    /**
     * Returns the center of the block over 3 dimensions
     * @param location The block where the center should be calculated from
     * @return The middle location of the given block
     */

    public static Location getCenter3D(Location location) { return location.add(0.5, -0.5, 0.5); }

    /**
     * Compare two locations with the given delta.
     * @param location1 The first location
     * @param location2 The second location
     * @param delta The maximum delta that is allowed between both locations
     * @return True, if the location between both locations is less than the given delta, false otherwise
     */

    public static boolean compare(Location location1, Location location2, double delta) {
        if (location1.equals(location2))
            return true;
        return location1.distanceSquared(location2) <= Math.pow(delta, 2);
    }

    public static boolean compare(Location location1, Location location2, boolean angle) {
        boolean position =  location1.getX() == location2.getX()
                && location1.getY() == location2.getY()
                && location1.getZ() == location2.getZ();

        if (!position)
            return false;

        if (angle) {
            return location1.getYaw() == location2.getYaw()
                    && location1.getPitch() == location2.getPitch();
        }
        return position;
    }

    /**
     * Checks if the block is solid at the location.
     * @param location The location to check
     * @return True when the block is solid otherwise false
     */

    public static boolean isSolid(Location location) {
        return location.getBlock().getType().isBlock();
    }
}