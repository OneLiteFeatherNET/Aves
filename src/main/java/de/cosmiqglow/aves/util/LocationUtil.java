package de.cosmiqglow.aves.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class LocationUtil {

    public static Location getCenter(Block block) {
        return block.getLocation().add(0.5, 0, 0.5);
    }
}