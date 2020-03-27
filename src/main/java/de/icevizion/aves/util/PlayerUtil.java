package de.icevizion.aves.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contains some methods to work with {@link Player} objects
 */
public class PlayerUtil {

    /**
     * Choose a random player from all players which are currently online
     * @return a random player
     */

    public static Optional<Player> getRandomPlayer() {
        return Bukkit.getOnlinePlayers().stream().collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected);
            return collected.stream();
        })).map(t -> ((Player) t)).findAny();
    }
}