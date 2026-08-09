package net.theevilreaper.aves.map.provider;

import com.google.gson.GsonBuilder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.file.GsonFileHandler;
import net.theevilreaper.aves.file.gson.PositionGsonAdapter;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.map.impl.TestSwitchingMapProvider;
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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MicrotusExtension.class)
class SwitchingMapProviderTest {

    private static Path rootPath;
    private static FileHandler fileHandler;
    private static PathFilter<MapEntry> pathFilter;

    private TestSwitchingMapProvider mapProvider;

    @BeforeAll
    static void init() throws URISyntaxException {
        URL resourceUrl = SwitchingMapProviderTest.class.getClassLoader().getResource("map");
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
        this.mapProvider = new TestSwitchingMapProvider(rootPath, fileHandler, pathFilter);
    }

    @Test
    void switchToMovesCurrentActiveToPreviousAndActivatesNewOnes(@NotNull Env env) {
        InstanceContainer firstInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        BaseMap firstMap = BaseMap.builder().name("first").build();
        this.mapProvider.doSwitch(firstInstance, firstMap);

        InstanceContainer secondInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        BaseMap secondMap = BaseMap.builder().name("second").build();
        this.mapProvider.doSwitch(secondInstance, secondMap);

        assertSame(secondInstance, this.mapProvider.currentActiveInstance());
        assertSame(secondMap, this.mapProvider.currentActiveMap());
        assertSame(firstInstance, this.mapProvider.currentPreviousInstance());

        env.destroyInstance(firstInstance, true);
        env.destroyInstance(secondInstance, true);
    }

    @Test
    void releasePreviousInstanceUnregistersAndClearsIt(@NotNull Env env) {
        InstanceContainer firstInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.mapProvider.doSwitch(firstInstance, BaseMap.builder().name("first").build());

        InstanceContainer secondInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.mapProvider.doSwitch(secondInstance, BaseMap.builder().name("second").build());

        this.mapProvider.releasePreviousInstance();

        assertNull(this.mapProvider.currentPreviousInstance());
        assertNull(MinecraftServer.getInstanceManager().getInstance(firstInstance.getUuid()));

        env.destroyInstance(secondInstance, true);
    }

    @Test
    void releasePreviousInstanceWithoutPriorSwitchIsNoOp() {
        assertDoesNotThrow(() -> this.mapProvider.releasePreviousInstance());
        assertNull(this.mapProvider.currentPreviousInstance());
    }

    @Test
    void releasePreviousInstanceIsIdempotent(@NotNull Env env) {
        InstanceContainer firstInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.mapProvider.doSwitch(firstInstance, BaseMap.builder().name("first").build());

        InstanceContainer secondInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.mapProvider.doSwitch(secondInstance, BaseMap.builder().name("second").build());

        this.mapProvider.releasePreviousInstance();
        assertDoesNotThrow(() -> this.mapProvider.releasePreviousInstance());

        env.destroyInstance(secondInstance, true);
    }
}
