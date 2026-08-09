package net.theevilreaper.aves.map.provider;

import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.util.functional.PathFilter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.anvil.AnvilLoader;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Abstract base implementation of {@link MapProvider} for managing map lifecycles
 * within a server session. Handles loading map entries from disk, registering map
 * instances, and providing access to the currently active map and instance.
 * <p>
 * Subclasses should implement logic for switching maps and managing map-specific data.
 * </p>
 *
 * @author theEvilReaper
 * @version 1.3.0
 * @since 1.6.0
 */
public abstract class AbstractMapProvider implements MapProvider {

    private static final Logger MAP_LOGGER = LoggerFactory.getLogger(AbstractMapProvider.class);

    private final PathFilter<MapEntry> mapFilter;
    protected final FileHandler fileHandler;
    protected final List<MapEntry> mapEntries;
    protected final List<Closeable> closeableChunkLoaders;

    protected @Nullable BaseMap activeMap;
    protected @Nullable InstanceContainer activeInstance;

    /**
     * Constructs a BaseMapProvider with the specified FileHandler.
     *
     * @param fileHandler the {@link FileHandler} used to load and save maps
     * @param mapFilter   the filtering logic for the map entries
     */
    protected AbstractMapProvider(FileHandler fileHandler, PathFilter<MapEntry> mapFilter) {
        this.fileHandler = fileHandler;
        this.mapFilter = mapFilter;
        this.mapEntries = new ArrayList<>();
        this.closeableChunkLoaders = new ArrayList<>();
    }

    /**
     * Registers the specified map entry as an active instance in the server.
     * Sets up chunk loading and time rate, and registers the instance with the server manager.
     *
     * @param instance to be registered
     * @param mapEntry  representing the folder that contains the map files
     */
    protected void registerInstance(InstanceContainer instance, MapEntry mapEntry) {
        this.registerInstance(instance, mapEntry, DimensionType.OVERWORLD);
    }

    /**
     * Registers the specified map entry as an active instance in the server.
     * Sets up chunk loading and time rate, and registers the instance with the server manager.
     *
     * @param instance     to be registered
     * @param mapEntry     representing the folder that contains the map files
     * @param dimensionKey the dimension type key for the instance
     */
    protected void registerInstance(InstanceContainer instance, MapEntry mapEntry, RegistryKey<DimensionType> dimensionKey) {
        ChunkLoader chunkLoader = this.createChunkLoader(instance, mapEntry, dimensionKey);
        if (chunkLoader instanceof Closeable closeable) {
            this.closeableChunkLoaders.add(closeable);
        }

        instance.setChunkLoader(chunkLoader);
        instance.enableAutoChunkLoad(true);
        var defaultClock = instance.defaultClock();
        if (defaultClock != null) {
            defaultClock.rate(0f);
        }
        MinecraftServer.getInstanceManager().registerInstance(instance);
    }

    /**
     * Creates the {@link ChunkLoader} used for the given map entry when it is registered as an instance.
     * <p>
     * The default implementation returns Minestom's own {@link AnvilLoader}. Override this method to plug in a
     * different chunk loader implementation (e.g. one that reads region files in parallel). If the returned
     * loader implements {@link Closeable}, it is tracked automatically and closed by {@link #close()}.
     *
     * @param instance     the instance the map is loaded into
     * @param mapEntry     representing the folder that contains the map files
     * @param dimensionKey the dimension type key for the instance
     * @return the chunk loader to attach to the instance
     */
    protected ChunkLoader createChunkLoader(InstanceContainer instance, MapEntry mapEntry, RegistryKey<DimensionType> dimensionKey) {
        return new AnvilLoader(mapEntry.getDirectoryRoot(), dimensionKey.key());
    }

    /**
     * Closes every {@link Closeable} chunk loader created via {@link #createChunkLoader(InstanceContainer, MapEntry, RegistryKey)}.
     * Calling this more than once is harmless.
     */
    public void close() {
        for (Closeable closeable : this.closeableChunkLoaders) {
            try {
                closeable.close();
            } catch (IOException exception) {
                MinecraftServer.getExceptionManager().handleException(exception);
            }
        }
        this.closeableChunkLoaders.clear();
    }

    /**
     * Loads the {@link BaseMap} data for the given map entry and creates a registered {@link InstanceContainer}
     * for it, without changing {@link #activeMap} or {@link #activeInstance}.
     * <p>
     * Callers decide whether the result becomes the active map (initial load) or is passed to a switch
     * mechanism (see {@link SwitchingMapProvider}).
     *
     * @param mapEntry the entry to load; must have a map file
     * @param type     the concrete {@link BaseMap} subtype to deserialize into
     * @param <T>      the map type
     * @return the loaded map together with its freshly registered instance
     * @throws IllegalStateException if the entry has no map file or the file could not be parsed
     */
    protected <T extends BaseMap> LoadedMap<T> loadMapEntry(MapEntry mapEntry, Class<T> type) {
        if (!mapEntry.hasMapFile()) {
            throw new IllegalStateException("Map entry does not contain a map file: " + mapEntry.getDirectoryRoot());
        }

        T map = this.fileHandler.load(mapEntry.getMapFile(), type)
                .orElseThrow(() -> new IllegalStateException("Failed to load map from file: " + mapEntry.getMapFile()));
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.registerInstance(instance, mapEntry);
        return new LoadedMap<>(map, instance);
    }

    /**
     * The result of {@link #loadMapEntry(MapEntry, Class)}: a loaded map paired with the instance it was
     * registered into.
     *
     * @param map      the loaded map data
     * @param instance the registered instance
     * @param <T>      the map type
     */
    protected record LoadedMap<T extends BaseMap>(T map, InstanceContainer instance) {
    }

    /**
     * Loads all available map entries from the specified directory.
     * Only directories passing the configured map filter are included.
     * Handles IO exceptions gracefully by logging and reporting to the server exception manager.
     *
     * @param path the root directory containing map folders; must not be null
     */
    protected void loadMapEntries(Path path) {
        this.mapEntries.clear();

        try (Stream<Path> stream = Files.list(path)) {
            this.mapEntries.addAll(
                    this.mapFilter.filter(
                            stream.filter(Files::isDirectory)
                    )
            );
        } catch (IOException exception) {
            MinecraftServer.getExceptionManager().handleException(exception);
            MAP_LOGGER.error("Unable to load maps from path {}", path, exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teleportToSpawn(Player player, boolean instanceSet) {
        Pos pos = activeMap().getSpawnOrDefault(FALLBACK_POS);

        if (!instanceSet) {
            player.teleport(pos);
            return;
        }

        player.setInstance(activeInstance(), pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @UnmodifiableView List<MapEntry> getEntries() {
        return Collections.unmodifiableList(this.mapEntries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Supplier<@Nullable Instance> getActiveInstance() {
        return () -> this.activeInstance;
    }

    /**
     * Returns the currently active map.
     *
     * @return the active map
     * @throws IllegalStateException if no active map is set
     */
    protected BaseMap activeMap() {
        if (activeMap == null) {
            throw new IllegalStateException("Active map has not been initialized yet");
        }
        return activeMap;
    }

    /**
     * Returns the currently active instance.
     *
     * @return the active instance
     * @throws IllegalStateException if no active instance is set
     */
    protected InstanceContainer activeInstance() {
        if (activeInstance == null) {
            throw new IllegalStateException("Active instance has not been initialized yet");
        }
        return activeInstance;
    }
}

