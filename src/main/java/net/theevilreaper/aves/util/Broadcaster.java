package net.theevilreaper.aves.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * The class allows the broadcasting of a message to all player on the server, to an instance or to a group of players.
 * @author theEvilReaper
 * @since 1.2.0
 * @version 1.0.0
 */
@ApiStatus.NonExtendable
public final class Broadcaster {

    private Broadcaster() {}

    /**
     * Broadcasts a message to all players on the server
     * @param component The message to send
     */
    @ApiStatus.Experimental
    public static void broadcast(@NotNull Component component) {
        broadcast(MinecraftServer.getConnectionManager().getOnlinePlayers(), component);
    }

    /**
     * Broadcasts a message as to an {@link Instance}.
     * @param instance The instance to get the players from it
     * @param message The message to send
     */
    @ApiStatus.Experimental
    public static void broadcast(@NotNull Instance instance, @NotNull Component message) {
        broadcast(instance.getPlayers(), message);
    }

    /**
     * Broadcasts a message as to a group of players
     * @param players The player who receives the message
     * @param message The message to send
     */
    @ApiStatus.Experimental
    public static void broadcast(@NotNull Collection<Player> players, @NotNull Component message) {
        if (players.isEmpty()) return;

        for (Player player : players) {
            player.sendMessage(message);
        }
    }
}
