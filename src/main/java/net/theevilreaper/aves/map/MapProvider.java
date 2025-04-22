package net.theevilreaper.aves.map;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

/**
 * The {@link MapProvider} interface is responsible for managing the available maps.
 * It will load all maps data from the given path and store them.
 * It would not load the map itself over a {@link net.minestom.server.instance.anvil.AnvilLoader} instance.
 * This behavior is handled by another class.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.6.0
 */
public interface MapProvider {

    Pos FALLBACK_POS = new Pos(0, 100, 0);

    /**
     * Saves the given data from a {@link BaseMap} to the given path.
     *
     * @param path    the path where the map data should be saved
     * @param baseMap the map data which should be saved
     */
    void saveMap(@NotNull Path path, @NotNull BaseMap baseMap);

    /**
     * Teleports a {@link Player} to the current active spawn position of the {@link Instance}.
     *
     * @param player      the player which should be teleported
     * @param instanceSet if the instance should be set to the current active instance
     */
    void teleportToSpawn(@NotNull Player player, boolean instanceSet);

    /**
     * Returns a {@link List} which contains all available maps as {@link MapEntry} objects.
     *
     * @return the given list of maps
     */
    @UnmodifiableView
    @NotNull List<MapEntry> getEntries();

    /**
     * Returns the current active {@link Instance} which is used for the game.
     *
     * @return the current active instance
     */
    @NotNull Supplier<@Nullable Instance> getActiveInstance();
}
