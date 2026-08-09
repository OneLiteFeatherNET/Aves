package net.theevilreaper.aves.map.provider;

import com.google.gson.GsonBuilder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.file.GsonFileHandler;
import net.theevilreaper.aves.file.gson.PositionGsonAdapter;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.map.impl.TestChunkLoaderMapProvider;
import net.theevilreaper.aves.util.functional.PathFilter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.Closeable;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MicrotusExtension.class)
class AbstractMapProviderChunkLoaderTest {

    private static Path rootPath;
    private static FileHandler fileHandler;
    private static PathFilter<MapEntry> pathFilter;

    private TestChunkLoaderMapProvider mapProvider;

    @BeforeAll
    static void init() throws URISyntaxException {
        URL resourceUrl = AbstractMapProviderChunkLoaderTest.class.getClassLoader().getResource("map");
        rootPath = Paths.get(resourceUrl.toURI());

        PositionGsonAdapter positionGsonAdapter = new PositionGsonAdapter();
        fileHandler = new GsonFileHandler(
                new GsonBuilder()
                        .registerTypeAdapter(Pos.class, positionGsonAdapter)
                        .registerTypeAdapter(Vec.class, positionGsonAdapter)
                        .create()
        );

        pathFilter = stream -> stream
                .filter(Files::isDirectory)
                .filter(path -> Files.exists(path.resolve("map.json")))
                .map(MapEntry::of)
                .toList();
    }

    @BeforeEach
    void setUp() {
        this.mapProvider = new TestChunkLoaderMapProvider(rootPath, fileHandler, pathFilter);
    }

    @Test
    void registerInstanceUsesChunkLoaderFromHook(@NotNull Env env) {
        ChunkLoader marker = new NoopChunkLoader();
        this.mapProvider.useChunkLoader(entry -> marker);

        MapEntry mapEntry = this.mapProvider.getEntries().getFirst();
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();

        this.mapProvider.registerFromEntry(instance, mapEntry);

        assertSame(marker, instance.getChunkLoader());

        env.destroyInstance(instance, true);
    }

    @Test
    void closeClosesChunkLoadersThatImplementCloseable(@NotNull Env env) {
        CountingCloseableChunkLoader loader = new CountingCloseableChunkLoader();
        this.mapProvider.useChunkLoader(entry -> loader);

        MapEntry mapEntry = this.mapProvider.getEntries().getFirst();
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.mapProvider.registerFromEntry(instance, mapEntry);

        this.mapProvider.close();

        assertEquals(1, loader.closeCount);

        env.destroyInstance(instance, true);
    }

    @Test
    void closeIsIdempotent(@NotNull Env env) {
        CountingCloseableChunkLoader loader = new CountingCloseableChunkLoader();
        this.mapProvider.useChunkLoader(entry -> loader);

        MapEntry mapEntry = this.mapProvider.getEntries().getFirst();
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.mapProvider.registerFromEntry(instance, mapEntry);

        this.mapProvider.close();
        this.mapProvider.close();

        assertEquals(1, loader.closeCount);

        env.destroyInstance(instance, true);
    }

    private static final class NoopChunkLoader implements ChunkLoader {
        @Override
        public Chunk loadChunk(Instance instance, int chunkX, int chunkZ) {
            return null;
        }

        @Override
        public void saveChunk(Chunk chunk) {
        }
    }

    private static final class CountingCloseableChunkLoader implements ChunkLoader, Closeable {
        private int closeCount = 0;

        @Override
        public Chunk loadChunk(Instance instance, int chunkX, int chunkZ) {
            return null;
        }

        @Override
        public void saveChunk(Chunk chunk) {
        }

        @Override
        public void close() throws IOException {
            this.closeCount++;
        }
    }
}
