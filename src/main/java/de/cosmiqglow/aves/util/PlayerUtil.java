package de.cosmiqglow.aves.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

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
}