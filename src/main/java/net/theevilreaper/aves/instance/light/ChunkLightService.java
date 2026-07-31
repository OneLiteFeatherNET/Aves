package net.theevilreaper.aves.instance.light;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Section;
import net.minestom.server.instance.palette.Palette;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link ChunkLightService} class calculates the block light of a chunk and hands the result to
 * the sections of that chunk.
 * <p>
 * The service is the connection between the engine and a running server. It reads the block states
 * of every section, runs the propagation and writes the result back through
 * {@link net.minestom.server.instance.light.Light#set(byte[])}. That method belongs to the stable
 * part of the light interface, which is why the service uses it instead of implementing the
 * interface itself: the calculation methods of that interface are marked internal and their
 * signatures may change between server versions.
 * </p>
 * <p>
 * Because the result is handed over through the regular interface, the service works with any chunk
 * of any loader, including chunks produced by the Anvil loader of Aves or the one of the server.
 * </p>
 * <p>
 * Writing the light through {@code set} also clears the update flag of the section, so the server
 * does not recompute what was just calculated.
 * </p>
 * <p>
 * This type is experimental. The light engine is new and its API may still change.
 * </p>
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.16.0
 */
@ApiStatus.Experimental
public final class ChunkLightService {

    private final BlockLightSource source;
    private final ChunkLightPropagator propagator;

    /**
     * Creates a service which reads the block properties from the registry of the server.
     */
    public ChunkLightService() {
        this(new MinestomBlockLightSource());
    }

    /**
     * Creates a service which reads the block properties from the given source.
     *
     * @param source the source which describes the light properties of a block
     */
    public ChunkLightService(BlockLightSource source) {
        this.source = source;
        this.propagator = new ChunkLightPropagator();
    }

    /**
     * Calculates the block light of the given chunk and stores it in its sections.
     * <p>
     * The block states are read under the read lock of the chunk, the propagation runs without any
     * lock, and only the transfer of the result is guarded again. The expensive part therefore
     * never blocks another user of the chunk.
     * </p>
     *
     * @param chunk the chunk to light
     */
    public void calculate(Chunk chunk) {
        List<int[]> states = readStates(chunk);
        List<SectionOpacity> opacity = new ArrayList<>(states.size());

        for (int[] section : states) {
            opacity.add(SectionOpacity.of(section, this.source));
        }

        List<LightNibbles> light = this.propagator.propagate(opacity);

        chunk.lockWriteLock();
        try {
            List<Section> sections = chunk.getSections();

            for (int index = 0; index < sections.size() && index < light.size(); index++) {
                sections.get(index).blockLight().set(light.get(index).toDenseArray());
            }
        } finally {
            chunk.unlockWriteLock();
        }
    }

    /**
     * Returns the block light level which is stored for the given position.
     *
     * @param chunk the chunk which holds the position
     * @param x     the x coordinate inside the chunk
     * @param y     the y coordinate of the block
     * @param z     the z coordinate inside the chunk
     * @return the stored light level of the position
     */
    @Contract(pure = true)
    public int blockLightAt(Chunk chunk, int x, int y, int z) {
        chunk.lockReadLock();
        try {
            return chunk.getSectionAt(y).blockLight().getLevel(x & 15, y & 15, z & 15);
        } finally {
            chunk.unlockReadLock();
        }
    }

    /**
     * Reads the block state of every block of every section of the chunk.
     *
     * @param chunk the chunk to read
     * @return the state ids of every section, ordered from the lowest section upwards
     */
    private static List<int[]> readStates(Chunk chunk) {
        chunk.lockReadLock();
        try {
            List<Section> sections = chunk.getSections();
            List<int[]> states = new ArrayList<>(sections.size());

            for (Section section : sections) {
                int[] blocks = new int[LightNibbles.BLOCK_COUNT];
                Palette palette = section.blockPalette();
                palette.getAll((x, y, z, value) -> blocks[(y << 8) | (z << 4) | x] = value);
                states.add(blocks);
            }
            return states;
        } finally {
            chunk.unlockReadLock();
        }
    }
}
