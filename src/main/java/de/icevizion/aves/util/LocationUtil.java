package de.icevizion.aves.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Contains some methods that work with the @{@link Location}
 */
public class LocationUtil {

    /**
     * Returns the center of the block.
     * @param block The block where the center should be calculated from
     * @return The middle location of the given block
     */

    public static Location getCenter(Block block) {
        return block.getLocation().add(0.5, 0, 0.5);
    }

    /**
     * Compare two locations with the given delta
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
}