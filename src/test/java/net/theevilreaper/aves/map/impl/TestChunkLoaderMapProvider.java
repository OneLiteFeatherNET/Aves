package net.theevilreaper.aves.map.impl;

import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.map.provider.AbstractMapProvider;
import net.theevilreaper.aves.util.functional.PathFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.function.Function;

public final class TestChunkLoaderMapProvider extends AbstractMapProvider {

    private @Nullable Function<MapEntry, ChunkLoader> chunkLoaderFactory;

    public TestChunkLoaderMapProvider(@NotNull Path path, @NotNull FileHandler fileHandler, @NotNull PathFilter<MapEntry> mapFilter) {
        super(fileHandler, mapFilter);
        this.loadMapEntries(path);
    }

    public void useChunkLoader(Function<MapEntry, ChunkLoader> factory) {
        this.chunkLoaderFactory = factory;
    }

    public void registerFromEntry(InstanceContainer instance, MapEntry mapEntry) {
        this.registerInstance(instance, mapEntry);
    }

    public <T extends BaseMap> LoadedMap<T> loadEntry(MapEntry mapEntry, Class<T> type) {
        return this.loadMapEntry(mapEntry, type);
    }

    public @Nullable BaseMap currentActiveMap() {
        return this.activeMap;
    }

    public @Nullable InstanceContainer currentActiveInstance() {
        return this.activeInstance;
    }

    @Override
    protected ChunkLoader createChunkLoader(InstanceContainer instance, MapEntry mapEntry, RegistryKey<DimensionType> dimensionKey) {
        return this.chunkLoaderFactory != null
                ? this.chunkLoaderFactory.apply(mapEntry)
                : super.createChunkLoader(instance, mapEntry, dimensionKey);
    }

    @Override
    public void saveMap(@NotNull Path path, @NotNull BaseMap baseMap) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
