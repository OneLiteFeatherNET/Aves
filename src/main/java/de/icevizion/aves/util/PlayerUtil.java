package de.icevizion.aves.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Random;

/**
 * Contains some methods to work with {@link Player} objects
 */
public class PlayerUtil {

    /**
     * Choose a random player from all players which are currently online
     * @return a random player
     */

    public static Optional<Player> getRandomPlayer() {
        Random random = new Random(Bukkit.getOnlinePlayers().size());
        return (Optional<Player>) Bukkit.getOnlinePlayers().stream().skip(Bukkit.getOnlinePlayers().size() * random.nextInt()).findAny();
    }
}