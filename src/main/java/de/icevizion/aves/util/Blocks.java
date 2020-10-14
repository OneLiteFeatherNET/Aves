package de.icevizion.aves.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The class includes some auxiliary methods to do something with blocks
 */

public class Blocks {

    /**
     * Gets the BlockFace of the block the player is currently targeting.
     *
     * @param player the player's who's targeted blocks BlockFace is to be checked.
     * @param distance the distance to scan
     * @return the BlockFace of the targeted block, or null if the targeted block is non-occluding.
     */

    public static BlockFace getBlockFace(Player player, int distance) {
        if (distance > 100) {
            throw new IllegalArgumentException("The distance to scan must between 0 and 100");
        }
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, distance);
        return lastTwoTargetBlocks.get(1).getFace(lastTwoTargetBlocks.get(0));
    }
}