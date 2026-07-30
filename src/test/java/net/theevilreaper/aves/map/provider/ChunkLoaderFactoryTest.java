package net.theevilreaper.aves.map.provider;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.ChunkLoader;
import net.theevilreaper.aves.instance.anvil.AvesAnvilLoader;
import net.theevilreaper.aves.map.MapEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests the factory which lets a map provider choose the chunk loader of an instance.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.16.0
 */
class ChunkLoaderFactoryTest {

    private static final Key OVERWORLD = Key.key("minecraft:overworld");

    @Test
    void testTheAnvilFactoryCreatesTheAvesLoader(@TempDir Path tempDir) throws IOException {
        Files.createDirectories(tempDir.resolve("world"));
        MapEntry entry = MapEntry.of(tempDir.resolve("world"));

        ChunkLoader loader = ChunkLoaderFactory.anvil().create(entry, OVERWORLD);

        assertNotNull(loader);
        assertInstanceOf(AvesAnvilLoader.class, loader);
        ((AvesAnvilLoader) loader).close();
    }

    @Test
    void testACustomFactoryIsUsedAsGiven(@TempDir Path tempDir) throws IOException {
        Files.createDirectories(tempDir.resolve("world"));
        MapEntry entry = MapEntry.of(tempDir.resolve("world"));
        ChunkLoader expected = ChunkLoader.noop();

        ChunkLoaderFactory factory = (mapEntry, dimension) -> expected;

        assertEquals(expected, factory.create(entry, OVERWORLD));
    }

    @Test
    void testTheFactoryReceivesTheDirectoryOfTheEntry(@TempDir Path tempDir) throws IOException {
        Path world = tempDir.resolve("world");
        Files.createDirectories(world);
        MapEntry entry = MapEntry.of(world);

        ChunkLoaderFactory factory = (mapEntry, dimension) -> {
            assertEquals(world, mapEntry.getDirectoryRoot());
            assertEquals(OVERWORLD, dimension);
            return ChunkLoader.noop();
        };

        assertNotNull(factory.create(entry, OVERWORLD));
    }
}
