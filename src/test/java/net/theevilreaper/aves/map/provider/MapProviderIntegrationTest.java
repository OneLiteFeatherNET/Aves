package net.theevilreaper.aves.map.provider;

import com.google.gson.GsonBuilder;
import net.theevilreaper.aves.TestMapProvider;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.file.GsonFileHandler;
import net.theevilreaper.aves.file.gson.PositionGsonAdapter;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.objects.TestMapProvider;
import net.theevilreaper.aves.util.functional.PathFilter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MicrotusExtension.class)
class MapProviderIntegrationTest {

    private static Path rootPath;
    private static FileHandler fileHandler;
    private static PathFilter<MapEntry> pathFilter;

    private MapProvider mapProvider;

    @BeforeAll
    static void init() throws URISyntaxException {
        URL resourceUrl = MapProviderIntegrationTest.class.getClassLoader().getResource("map");
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
        this.mapProvider = new TestMapProvider(rootPath, fileHandler, pathFilter);
        assertNotNull(mapProvider);
        assertInstanceOf(MapProvider.class, mapProvider);
        assertInstanceOf(AbstractMapProvider.class, mapProvider);
        assertFalse(mapProvider.getEntries().isEmpty());
        assertEquals(
                1,
                mapProvider.getEntries().size(),
                "At provider instance creation, the map entries should be 1"
        );
    }

    @AfterEach
    void tearDown() {
        mapProvider = null;
    }

    @AfterAll
    static void tearDownAll() {
        fileHandler = null;
        pathFilter = null;
    }

    @Disabled("Disabled due to long execution time")
    @Test
    void testMapHandlingLogic(@NotNull Env env) {
        InstanceContainer instance = (InstanceContainer) env.createFlatInstance();
        Player player = env.createPlayer(instance);
        assertNotNull(instance);

        MapEntry mapEntry = mapProvider.getEntries().getFirst();
        assertNotNull(mapEntry, "Map entry should not be null");

        ((TestMapProvider) mapProvider).loadMap(mapEntry);

        assertNotNull(mapProvider.getActiveInstance());

        mapProvider.teleportToSpawn(player, true);

        for (int i = 0; i < 20; i++) {
            env.tick();
        }

        // assertNotEquals(instance.getUuid(), player.getInstance().getUuid());
        // assertNotEquals(Pos.ZERO, player.getPosition());

        player.teleport(Pos.ZERO);

        assertEquals(Pos.ZERO, player.getPosition());

        mapProvider.teleportToSpawn(player, false);

        assertNotEquals(Pos.ZERO, player.getPosition());

        env.destroyInstance(instance, true);
        env.destroyInstance(mapProvider.getActiveInstance().get(), true);
    }
}

