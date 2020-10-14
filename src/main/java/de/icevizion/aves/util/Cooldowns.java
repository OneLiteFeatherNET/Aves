package de.icevizion.aves.util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Cooldowns {

    private final Map<Player, Long> cooldowns;

    public Cooldowns() {
        this.cooldowns = new HashMap<>();
    }

    /**
     * Add a cooldown to a specific player.
     * @param player The player for the cooldown
     * @param duration The duration for the cooldown
     */

    public void add(Player player, long duration) {
        this.cooldowns.putIfAbsent(player, System.currentTimeMillis() + duration);
    }

    /**
     * Remove a cooldown from a player.
     * @param player The player to remove
     */

    public void remove(Player player) {
        this.cooldowns.remove(player);
    }

    /**
     * Check if a player has currently a cooldown.
     * @param player The player to check
     * @return True when the player has a cooldown otherwise false
     */

    public boolean has(Player player) {
        return cooldowns.getOrDefault(player, 0L) > 0;
    }
}