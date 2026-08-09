package net.theevilreaper.aves.map.provider;

import com.google.gson.GsonBuilder;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.file.GsonFileHandler;
import net.theevilreaper.aves.file.gson.PositionGsonAdapter;
import net.theevilreaper.aves.map.BaseMap;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MicrotusExtension.class)
class AbstractMapProviderLoadMapEntryTest {

    private static Path rootPath;
    private static Path emptyMapDirectory;
    private static FileHandler fileHandler;
    private static PathFilter<MapEntry> pathFilter;

    private TestChunkLoaderMapProvider mapProvider;

    @BeforeAll
    static void init() throws URISyntaxException {
        URL resourceUrl = AbstractMapProviderLoadMapEntryTest.class.getClassLoader().getResource("map");
        rootPath = Paths.get(resourceUrl.toURI());
        emptyMapDirectory = rootPath.resolve("TestMap");

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
    void loadMapEntryLoadsMapAndRegistersInstanceWithoutTouchingActiveFields(@NotNull Env env) {
        MapEntry mapEntry = this.mapProvider.getEntries().getFirst();

        AbstractMapProvider.LoadedMap<BaseMap> loaded = this.mapProvider.loadEntry(mapEntry, BaseMap.class);

        assertNotNull(loaded.map());
        assertNotNull(loaded.instance());
        assertNull(this.mapProvider.currentActiveMap());
        assertNull(this.mapProvider.currentActiveInstance());

        env.destroyInstance(loaded.instance(), true);
    }

    @Test
    void loadMapEntryThrowsWhenEntryHasNoMapFile() {
        MapEntry entryWithoutMapFile = MapEntry.of(emptyMapDirectory, "does-not-exist.json");

        assertThrows(IllegalStateException.class, () -> this.mapProvider.loadEntry(entryWithoutMapFile, BaseMap.class));
    }
}
