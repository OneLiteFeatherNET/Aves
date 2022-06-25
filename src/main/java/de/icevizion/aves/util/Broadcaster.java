package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.ConnectionManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * The class allows the broadcasting of a message to all player on the server, to an instance or to a group of players.
 * @author theEvilReaper
 * @since 1.2.0
 * @version 1.0.0
 */
public final class Broadcaster {

    private static final ConnectionManager CONNECTION_MANAGER = MinecraftServer.getConnectionManager();

    /**
     * Broadcasts a message to all players on the server
     * @param message The message to send
     */
    @ApiStatus.Experimental
    public static void broadcast(@NotNull String message) {
        broadcast(CONNECTION_MANAGER.getOnlinePlayers(), message);
    }

    /**
     * Broadcasts a message to all players on the server
     * @param component The message to send
     */
    @ApiStatus.Experimental
    public static void broadcast(@NotNull Component component) {
        broadcast(CONNECTION_MANAGER.getOnlinePlayers(), component);
    }

    /**
     * Broadcasts a message as to an {@link Instance}.
     * @param instance The instance to get the players from it
     * @param message The message to send
     */
    @ApiStatus.Experimental
    public static void broadcast(@NotNull Instance instance, @NotNull String message) {
        broadcast(instance.getPlayers(), message);
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

    /**
     * Broadcasts a message as to a group of players
     * @param players The player who receives the message
     * @param message The message to send
     */
    @ApiStatus.Experimental
    public static void broadcast(@NotNull Collection<Player> players, @NotNull String message) {
        if (message.trim().isEmpty() || players.isEmpty()) return;

        for (Player player : players) {
            player.sendMessage(message);
        }
    }
}
