package de.icevizion.aves.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Contains some methods to work with {@link Player} objects
 */
public class PlayerUtil {

    /**
     * Send an actionbar to a specific player
     * @param player which receive the message
     * @param message to be send
     */

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    /**
     * Choose a random player from all players which are currently online
     * @return a random player
     */

    public static Optional<Player> getRandomPlayer() {
        return Optional.ofNullable(Bukkit.getOnlinePlayers().stream().skip((int)
                (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null));
    }
}