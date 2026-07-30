package net.theevilreaper.aves.instance.anvil;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the chunk loader against a running Minestom environment. The tests cover the round trip
 * of a chunk through the region file which is the behaviour the loader exists for.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.16.0
 */
@ExtendWith(MicrotusExtension.class)
class AvesAnvilLoaderIntegrationTest {

    private static final Key OVERWORLD = Key.key("minecraft:overworld");

    @TempDir
    private Path worldRoot;

    /**
     * Creates a loader for the temporary world of the test.
     *
     * @return the created loader
     */
    private AvesAnvilLoader loader() {
        return new AvesAnvilLoader(this.worldRoot, OVERWORLD);
    }

    @Test
    void testLoadingAnAbsentChunkReturnsNull(Env env) throws IOException {
        try (AvesAnvilLoader loader = loader()) {
            Instance instance = env.createEmptyInstance(loader);

            assertNull(loader.loadChunk(instance, 0, 0));
        }
    }

    @Test
    void testTheLoaderReportsParallelSupport() throws IOException {
        try (AvesAnvilLoader loader = loader()) {
            assertTrue(loader.supportsParallelLoading());
            assertTrue(loader.supportsParallelSaving());
        }
    }

    @Test
    void testASavedChunkKeepsItsBlocks(Env env) throws IOException {
        Instance instance = env.createEmptyInstance(loader());
        Chunk chunk = instance.loadChunk(2, 3).join();
        place(chunk, 0, 40, 0, Block.STONE);
        place(chunk, 5, 41, 7, Block.DIRT);
        place(chunk, 15, 42, 15, Block.OAK_PLANKS);

        try (AvesAnvilLoader writer = loader()) {
            writer.saveChunk(chunk);
        }

        try (AvesAnvilLoader reader = loader()) {
            Chunk loaded = reader.loadChunk(instance, 2, 3);

            assertNotNull(loaded);
            assertEquals(Block.STONE, blockAt(loaded, 0, 40, 0));
            assertEquals(Block.DIRT, blockAt(loaded, 5, 41, 7));
            assertEquals(Block.OAK_PLANKS, blockAt(loaded, 15, 42, 15));
            assertEquals(Block.AIR, blockAt(loaded, 1, 40, 0));
        }
    }

    @Test
    void testTheRegionFileIsCreatedInTheDimensionDirectory(Env env) throws IOException {
        Instance instance = env.createEmptyInstance(loader());
        Chunk chunk = instance.loadChunk(0, 0).join();
        place(chunk, 0, 40, 0, Block.STONE);

        try (AvesAnvilLoader writer = loader()) {
            writer.saveChunk(chunk);
        }

        Path expected = this.worldRoot.resolve("dimensions/minecraft/overworld/region/r.0.0.mca");
        assertTrue(Files.exists(expected), "expected a region file at " + expected);
    }

    @Test
    void testABlockWithNbtSurvivesTheRoundTrip(Env env) throws IOException {
        Instance instance = env.createEmptyInstance(loader());
        Chunk chunk = instance.loadChunk(0, 0).join();
        Block sign = Block.OAK_SIGN.withNbt(net.kyori.adventure.nbt.CompoundBinaryTag.builder()
                .putString("aves_marker", "kept")
                .build());
        place(chunk, 3, 45, 3, sign);

        try (AvesAnvilLoader writer = loader()) {
            writer.saveChunk(chunk);
        }

        try (AvesAnvilLoader reader = loader()) {
            Chunk loaded = reader.loadChunk(instance, 0, 0);

            assertNotNull(loaded);
            Block restored = blockAt(loaded, 3, 45, 3);
            assertEquals(Block.OAK_SIGN.key(), restored.key());
            assertEquals("kept", restored.nbtOrEmpty().getString("aves_marker"));
        }
    }

    @Test
    void testBlocksWithPropertiesKeepThem(Env env) throws IOException {
        Instance instance = env.createEmptyInstance(loader());
        Chunk chunk = instance.loadChunk(1, 1).join();
        Block slab = Block.OAK_SLAB.withProperty("type", "top");
        place(chunk, 4, 44, 4, slab);

        try (AvesAnvilLoader writer = loader()) {
            writer.saveChunk(chunk);
        }

        try (AvesAnvilLoader reader = loader()) {
            Chunk loaded = reader.loadChunk(instance, 1, 1);

            assertNotNull(loaded);
            assertEquals("top", blockAt(loaded, 4, 44, 4).getProperty("type"));
        }
    }

    @Test
    void testSavingManyChunksInParallelKeepsEveryOne(Env env) throws IOException, InterruptedException, ExecutionException {
        Instance instance = env.createEmptyInstance(loader());
        List<Chunk> chunks = new ArrayList<>();

        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                Chunk chunk = instance.loadChunk(x, z).join();
                place(chunk, 0, 40, 0, Block.STONE);
                place(chunk, 1, 40, 0, x + z == 0 ? Block.DIRT : Block.OAK_PLANKS);
                chunks.add(chunk);
            }
        }

        try (AvesAnvilLoader writer = loader()) {
            writer.saveChunks(chunks);
        }

        try (AvesAnvilLoader reader = loader()) {
            for (Chunk chunk : chunks) {
                Chunk loaded = reader.loadChunk(instance, chunk.getChunkX(), chunk.getChunkZ());

                assertNotNull(loaded, "chunk " + chunk.getChunkX() + "/" + chunk.getChunkZ() + " is missing");
                assertEquals(Block.STONE, blockAt(loaded, 0, 40, 0));
            }
        }
    }

    @Test
    void testLoadingInParallelReturnsEveryChunk(Env env) throws IOException, InterruptedException, ExecutionException {
        Instance instance = env.createEmptyInstance(loader());
        List<Chunk> chunks = new ArrayList<>();

        for (int x = 0; x < 4; x++) {
            Chunk chunk = instance.loadChunk(x, 0).join();
            place(chunk, 0, 40, 0, Block.STONE);
            chunks.add(chunk);
        }

        try (AvesAnvilLoader writer = loader()) {
            writer.saveChunks(chunks);
        }

        try (AvesAnvilLoader reader = loader();
             ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Chunk>> futures = new ArrayList<>();

            for (int x = 0; x < 4; x++) {
                int chunkX = x;
                futures.add(executor.submit(() -> reader.loadChunk(instance, chunkX, 0)));
            }
            for (Future<Chunk> future : futures) {
                assertNotNull(future.get());
            }
        }
    }

    @Test
    void testTheDiagnosticsCountTheProcessedChunks(Env env) throws IOException {
        Instance instance = env.createEmptyInstance(loader());
        Chunk chunk = instance.loadChunk(0, 0).join();
        place(chunk, 0, 40, 0, Block.STONE);

        try (AvesAnvilLoader loader = loader()) {
            loader.saveChunk(chunk);
            loader.loadChunk(instance, 0, 0);

            assertEquals(1, loader.diagnostics().chunksSaved());
            assertEquals(1, loader.diagnostics().chunksLoaded());
            assertEquals(0, loader.diagnostics().errors());
        }
    }

    @Test
    void testACorruptedChunkFailsInsteadOfLookingAbsent(Env env) throws IOException {
        Instance instance = env.createEmptyInstance(loader());
        Chunk chunk = instance.loadChunk(0, 0).join();
        place(chunk, 0, 40, 0, Block.STONE);

        try (AvesAnvilLoader writer = loader()) {
            writer.saveChunk(chunk);
        }

        // Overwrite the payload with bytes which are not a valid compressed chunk. Reporting this
        // as an absent chunk would make the server regenerate it and overwrite the real data.
        Path region = this.worldRoot.resolve("dimensions/minecraft/overworld/region/r.0.0.mca");
        byte[] bytes = Files.readAllBytes(region);
        java.util.Arrays.fill(bytes, RegionConstants.HEADER_SIZE + 5, bytes.length, (byte) 0x7F);
        Files.write(region, bytes);

        try (AvesAnvilLoader reader = loader()) {
            // The test environment turns a reported exception into an assertion error, so the test
            // asserts on the propagation itself instead of on a concrete type. What matters is that
            // the call does not return null, which would make the server regenerate the chunk.
            Throwable failure = assertThrows(Throwable.class, () -> reader.loadChunk(instance, 0, 0));

            assertNotNull(failure);
            assertEquals(1, reader.diagnostics().errors());
        }
    }

    @Test
    void testUnloadingAForeignChunkIsIgnored(Env env) throws IOException {
        Instance instance = env.createEmptyInstance(loader());
        Chunk chunk = instance.loadChunk(9, 9).join();

        try (AvesAnvilLoader loader = loader()) {
            loader.unloadChunk(chunk);
        }
    }

    /**
     * Places a block in the given chunk while holding its write lock.
     * The block setter of a chunk requires the caller to hold that lock.
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

    /**
     * Reads a block of the given chunk while holding its read lock.
     * The block getter of a chunk requires the caller to hold that lock.
     *
     * @param chunk the chunk to read
     * @param x     the x coordinate inside the chunk
     * @param y     the y coordinate of the block
     * @param z     the z coordinate inside the chunk
     * @return the block at the given position
     */
    private static Block blockAt(Chunk chunk, int x, int y, int z) {
        chunk.lockReadLock();
        try {
            return chunk.getBlock(x, y, z);
        } finally {
            chunk.unlockReadLock();
        }
    }
}
