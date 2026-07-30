package net.theevilreaper.aves.map.provider;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.ChunkLoader;
import net.theevilreaper.aves.instance.anvil.AvesAnvilLoader;
import net.theevilreaper.aves.map.MapEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * The {@link ChunkLoaderFactory} interface creates the {@link ChunkLoader} which a map provider
 * assigns to an instance.
 * <p>
 * The factory exists so a provider can choose its loader instead of being tied to a single
 * implementation. Existing code keeps the loader of Minestom while new code can opt into the
 * loader of Aves without any change to the provider itself.
 * </p>
 * <p>
 * This type is experimental. It is introduced together with the Anvil loader of Aves and its
 * API may still change while that loader is being validated against real worlds.
 * </p>
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.16.0
 */
@ApiStatus.Experimental
@FunctionalInterface
public interface ChunkLoaderFactory {

    /**
     * Returns a factory which creates the Anvil loader of Aves.
     *
     * @return the factory for the Anvil loader of Aves
     */
    @Contract(pure = true)
    static ChunkLoaderFactory anvil() {
        return (mapEntry, dimension) -> new AvesAnvilLoader(mapEntry.getDirectoryRoot(), dimension);
    }

    /**
     * Creates the chunk loader for the given map entry.
     *
     * @param mapEntry  the entry which describes the directory of the map
     * @param dimension the key of the dimension the instance uses
     * @return the created chunk loader
     */
    ChunkLoader create(MapEntry mapEntry, Key dimension);
}
