package net.theevilreaper.aves.map;

import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.util.functional.PathFilter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.anvil.AnvilLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @version 1.0.0
 * @since 1.6.0
 */
public abstract class AbstractMapProvider implements MapProvider {

    private static final Logger MAP_LOGGER = LoggerFactory.getLogger(AbstractMapProvider.class);

    private final PathFilter<MapEntry> mapFilter;

    protected FileHandler fileHandler;
    protected List<MapEntry> mapEntries;

    protected BaseMap activeMap;
    protected InstanceContainer activeInstance;

    /**
     * Constructs a BaseMapProvider with the specified FileHandler.
     *
     * @param fileHandler the {@link FileHandler} used to load and save maps
     * @param mapFilter   the filtering logic for the map entries
     */
    protected AbstractMapProvider(@NotNull FileHandler fileHandler, @NotNull PathFilter<MapEntry> mapFilter) {
        this.fileHandler = fileHandler;
        this.mapFilter = mapFilter;
    }

    /**
     * Registers the specified map entry as an active instance in the server.
     * Sets up chunk loading and time rate, and registers the instance with the server manager.
     *
     * @param instance the instance to be registered; must not be null
     * @param mapEntry the map entry representing the world data; must not be null
     */
    protected void registerInstance(@NotNull InstanceContainer instance, @NotNull MapEntry mapEntry) {
        instance.setChunkLoader(new AnvilLoader(mapEntry.getDirectoryRoot()));
        instance.enableAutoChunkLoad(true);
        instance.setTimeRate(0);
        MinecraftServer.getInstanceManager().registerInstance(instance);
    }

    /**
     * Loads all available map entries from the specified directory.
     * Only directories passing the configured map filter are included.
     * Handles IO exceptions gracefully by logging and reporting to the server exception manager.
     *
     * @param path the root directory containing map folders; must not be null
     * @return a list of map entries found in the directory, possibly empty but never null
     */
    protected @NotNull List<MapEntry> loadMapEntries(@NotNull Path path) {
        List<MapEntry> givenMaps = new ArrayList<>();
        try (Stream<Path> stream = Files.list(path)) {
            givenMaps.addAll(this.mapFilter.filter(stream.filter(Files::isDirectory)));
        } catch (IOException exception) {
            MinecraftServer.getExceptionManager().handleException(exception);
            MAP_LOGGER.error("Unable to load maps from path {}", path, exception);
        }
        return givenMaps;
    }

    @Override
    public void teleportToSpawn(@NotNull Player player, boolean instanceSet) {
        Pos pos = this.activeMap.getSpawnOrDefault(FALLBACK_POS);
        if (!instanceSet) {
            player.teleport(pos);
            return;
        }
        player.setInstance(this.activeInstance, pos);
    }

    @Override
    public @UnmodifiableView @NotNull List<MapEntry> getEntries() {
        if (this.mapEntries == null) return Collections.emptyList();
        return Collections.unmodifiableList(this.mapEntries);
    }

    @Override
    public @NotNull Supplier<@Nullable Instance> getActiveInstance() {
        return () -> this.activeInstance;
    }
}

