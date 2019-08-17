package de.cosmiqglow.aves.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class PlayerUtil {

    /**
     * Send a an actionbar to a specific player
     * @param player which receive the message
     * @param message to be send
     */

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}