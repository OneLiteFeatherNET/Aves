package net.theevilreaper.aves;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.map.AbstractMapProvider;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.util.functional.PathFilter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

public final class TestMapProvider extends AbstractMapProvider {
    /**
     * Constructs a BaseMapProvider with the specified FileHandler.
     *
     * @param path        the path to the map files
     * @param fileHandler the {@link FileHandler} used to load and save maps
     * @param mapFilter   the filtering logic for the map entries
     */
    public TestMapProvider(@NotNull Path path, @NotNull FileHandler fileHandler, @NotNull PathFilter<MapEntry> mapFilter) {
        super(fileHandler, mapFilter);
        this.mapEntries = loadMapEntries(path);
    }

    public void loadMap(@NotNull MapEntry mapFile) {
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();

        Optional<BaseMap> loadedMap = fileHandler.load(mapFile.getMapFile(), BaseMap.class);

        if (loadedMap.isEmpty()) {
            throw new IllegalStateException("Failed to load map from file: " + mapFile);
        }

        this.activeMap =  loadedMap.get();
        this.activeInstance = instance;

        // Set the map to the instance
        this.registerInstance(instance, mapFile);
    }

    @Override
    public void saveMap(@NotNull Path path, @NotNull BaseMap baseMap) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
