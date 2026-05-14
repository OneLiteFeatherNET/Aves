package net.theevilreaper.aves.map.provider;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

/**
 * The {@link MapProvider} interface is responsible for managing the available maps.
 * It will load all maps data from the given path and store them.
 * It would not load the map itself over a {@link AnvilLoader} instance.
 * This behavior is handled by another class.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 1.6.0
 */
public interface MapProvider {

    /**
     * A static fallback position that can be used if no spawn position is set.
     */
    Pos FALLBACK_POS = new Pos(0, 100, 0);

    /**
     * Saves the given data from a {@link BaseMap} to the given path.
     *
     * @param path    the path where the map data should be saved
     * @param baseMap the map data that should be saved
     */
    void saveMap(Path path, BaseMap baseMap);

    /**
     * Teleports a {@link Player} to the current active spawn position of the {@link Instance}.
     * This method does not change the player's instance.
     * To also set the instance, use {@link #teleportToSpawn(Player, boolean)}.
     *
     * @param player to teleport
     */
    default void teleportToSpawn(@NotNull Player player) {
        teleportToSpawn(player, false);
    }

    /**
     * Teleports a {@link Player} to the current active spawn position of the {@link Instance}.
     *
     * @param player      the player that should be teleported
     * @param instanceSet if the instance should be set to the current active instance
     */
    void teleportToSpawn(Player player, boolean instanceSet);

    /**
     * Returns a {@link List} which contains all available maps as {@link MapEntry} objects.
     *
     * @return the given list of maps
     */
    @UnmodifiableView
    List<MapEntry> getEntries();

    /**
     * Returns the current active {@link Instance} which is used for the game.
     *
     * @return the current active instance
     */
    Supplier<@Nullable Instance> getActiveInstance();
}
