package de.icevizion.aves.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * The class contains some methods that work with {@link Location}
 */
public class Locations {

    /**
     * Returns the center of the block.
     * @param block The block where the center should be calculated from
     * @return The middle location of the given block
     */

    @Deprecated
    public static Location getCenter(Block block) {
        return block.getLocation().add(0.5, 0, 0.5);
    }

    /**
     * Returns the center of the block over 3 dimensions
     * @param block The block where the center should be calculated from
     * @return The middle location of the given block
     */
    @Deprecated
    public static Location getCenter3D(Block block) { return block.getLocation().add(0.5, -0.5, 0.5); }

    /**
     * Compare two locations with the given delta.
     * @param location1 The first location
     * @param location2 The second location
     * @param delta The maximum delta that is allowed between both locations
     * @return True, if the location is the same with the given delta
     */

    public static boolean compare(Location location1, Location location2, double delta) {
        if (location1.equals(location2))
            return true;
        return location1.distanceSquared(location2) <= Math.pow(delta, 2);
    }

    /**
     * Compare two locations if they are the same, the angle can also be considered.
     * @param location1 The first location
     * @param location2 The second location
     * @param angle Whether the angle should be included
     * @return True, if the location between both locations is less than the given delta, false otherwise
     */

    public static boolean compare(Location location1, Location location2, boolean angle) {
        boolean position = location1.getX() == location2.getX()
                && location1.getY() == location2.getY()
                && location1.getZ() == location2.getZ();
        if (angle) {
            position = location1.getYaw() == location2.getYaw() && location1.getPitch() == location2.getPitch();
        }
        return position;
    }

    /**
     * Checks if 2 locations are equal, only the block locations are considered.
     * @param location The first location
     * @param location2 The second location
     * @return True, if both locations have the same coordinates otherwise false
     */

    public static boolean compare(Location location, Location location2) {
        return location.getBlockX() == location2.getBlockX()
                && location.getBlockY() == location2.getBlockY()
                && location.getBlockZ() == location2.getBlockZ();
    }

    /**
     * Checks if the block is solid at the location.
     * @param location The location to check
     * @return True when the block is solid otherwise false
     */

    public static boolean isSolid(Location location) {
        return location.getBlock().getType().isSolid();
    }

    /**
     * Checks if the block is a block on the location,
     * @param location The location
     * @return True when the block is a block otherwise false
     */

    public static boolean isBlock(Location location) { return location.getBlock().getType().isBlock(); }

    public static List<Location> addNeightBours(Location location) {
        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();

        List<Location> validLocations = new ArrayList<>();

        for (int y = -1; y < 2; y++) {
            for (int x =  -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    if (blockY + y < 1 || blockY + y > 255) continue;
                    Location current = new Location(location.getWorld(), blockX + x, blockY + y, blockZ + z);

                    validLocations.add(current);

                }
            }
        }

        return validLocations;


    }
}