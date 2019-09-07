package de.cosmiqglow.aves.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class LocationUtil {

    public static Location getCenter(Block block) {
        return block.getLocation().add(0.5, 0, 0.5);
    }

    public static boolean Compare(Location location1, Location location2, double delta) {
        return location1.distanceSquared(location2) < Math.pow(delta, 2);
    }
}