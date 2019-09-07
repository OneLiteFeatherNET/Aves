package de.cosmiqglow.aves.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class LocationUtil {

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
        return location1.distanceSquared(location2) < Math.pow(delta, 2);
    }
}