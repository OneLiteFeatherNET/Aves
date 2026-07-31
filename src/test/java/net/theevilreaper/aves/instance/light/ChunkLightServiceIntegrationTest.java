package net.theevilreaper.aves.instance.light;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that the light engine can be applied to a chunk of a running server, independent of the
 * chunk loader that produced the chunk.
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.16.0
 */
@ExtendWith(MicrotusExtension.class)
class ChunkLightServiceIntegrationTest {

    private final ChunkLightService service = new ChunkLightService();

    /**
     * Places a block in the given chunk while holding its write lock.
     *
     * @param chunk the chunk which receives the block
     * @param x     the x coordinate inside the chunk
     * @param y     the y coordinate of the block
     * @param z     the z coordinate inside the chunk
     * @param block the block to place
     */
    private static void place(Chunk chunk, int x, int y, int z, Block block) {
        chunk.lockWriteLock();
        try {
            chunk.setBlock(x, y, z, block);
        } finally {
            chunk.unlockWriteLock();
        }
    }

    @Test
    void testALampLightsItsSurroundings(Env env) {
        Instance instance = env.createEmptyInstance();
        Chunk chunk = instance.loadChunk(0, 0).join();
        place(chunk, 8, 40, 8, Block.GLOWSTONE);

        this.service.calculate(chunk);

        assertEquals(15, this.service.blockLightAt(chunk, 8, 40, 8));
        assertEquals(14, this.service.blockLightAt(chunk, 9, 40, 8));
        assertEquals(13, this.service.blockLightAt(chunk, 10, 40, 8));
    }

    @Test
    void testAChunkWithoutASourceStaysDark(Env env) {
        Instance instance = env.createEmptyInstance();
        Chunk chunk = instance.loadChunk(0, 0).join();

        this.service.calculate(chunk);

        assertEquals(0, this.service.blockLightAt(chunk, 8, 40, 8));
    }

    @Test
    void testTheLightIsWrittenIntoTheSectionsOfTheChunk(Env env) {
        Instance instance = env.createEmptyInstance();
        Chunk chunk = instance.loadChunk(0, 0).join();
        place(chunk, 8, 40, 8, Block.GLOWSTONE);

        this.service.calculate(chunk);

        // The engine has to hand its result to Minestom, not keep it on the side.
        chunk.lockReadLock();
        try {
            int sectionIndex = (40 >> 4) - chunk.getMinSection();
            byte[] stored = chunk.getSections().get(sectionIndex).blockLight().array();

            assertTrue(stored.length > 0, "the section has to carry the calculated light");
        } finally {
            chunk.unlockReadLock();
        }
    }

    @Test
    void testLightCrossesASectionBorderOfARealChunk(Env env) {
        Instance instance = env.createEmptyInstance();
        Chunk chunk = instance.loadChunk(0, 0).join();
        // Placed at the very top of its section so the light has to reach the one above.
        place(chunk, 8, 47, 8, Block.GLOWSTONE);

        this.service.calculate(chunk);

        assertEquals(15, this.service.blockLightAt(chunk, 8, 47, 8));
        assertEquals(14, this.service.blockLightAt(chunk, 8, 48, 8), "48 is the first block of the next section");
        assertEquals(13, this.service.blockLightAt(chunk, 8, 49, 8));
    }

    @Test
    void testAWallStopsTheLight(Env env) {
        Instance instance = env.createEmptyInstance();
        Chunk chunk = instance.loadChunk(0, 0).join();
        place(chunk, 8, 40, 8, Block.GLOWSTONE);

        // The wall has to span further than the light reaches in every direction, otherwise the
        // light simply travels around it and the test proves nothing.
        for (int y = 25; y <= 56; y++) {
            for (int z = 0; z < 16; z++) {
                place(chunk, 9, y, z, Block.STONE);
            }
        }

        this.service.calculate(chunk);

        assertEquals(0, this.service.blockLightAt(chunk, 10, 40, 8), "the wall has to stop the light");
        assertEquals(14, this.service.blockLightAt(chunk, 7, 40, 8), "the open side stays lit");
    }

    @Test
    void testCalculatingTwiceIsStable(Env env) {
        Instance instance = env.createEmptyInstance();
        Chunk chunk = instance.loadChunk(0, 0).join();
        place(chunk, 8, 40, 8, Block.GLOWSTONE);

        this.service.calculate(chunk);
        this.service.calculate(chunk);

        assertEquals(14, this.service.blockLightAt(chunk, 9, 40, 8));
    }

    @Test
    void testRemovingTheSourceClearsTheLight(Env env) {
        Instance instance = env.createEmptyInstance();
        Chunk chunk = instance.loadChunk(0, 0).join();
        place(chunk, 8, 40, 8, Block.GLOWSTONE);
        this.service.calculate(chunk);

        place(chunk, 8, 40, 8, Block.AIR);
        this.service.calculate(chunk);

        assertEquals(0, this.service.blockLightAt(chunk, 9, 40, 8), "a full recalculation has to retract the light");
    }

    @Test
    void testTheServiceWorksOnAChunkFromTheAnvilLoader(Env env, @org.junit.jupiter.api.io.TempDir java.nio.file.Path worldRoot) throws java.io.IOException {
        net.kyori.adventure.key.Key dimension = net.kyori.adventure.key.Key.key("minecraft:overworld");

        try (var loader = new net.theevilreaper.aves.instance.anvil.AvesAnvilLoader(worldRoot, dimension)) {
            Instance instance = env.createEmptyInstance(loader);
            Chunk chunk = instance.loadChunk(0, 0).join();
            place(chunk, 8, 40, 8, Block.GLOWSTONE);
            loader.saveChunk(chunk);

            Chunk reloaded = loader.loadChunk(instance, 0, 0);
            assertTrue(reloaded != null);

            this.service.calculate(reloaded);

            assertEquals(15, this.service.blockLightAt(reloaded, 8, 40, 8));
            assertEquals(14, this.service.blockLightAt(reloaded, 9, 40, 8));
        }
    }
}
