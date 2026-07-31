package net.theevilreaper.aves.instance.light;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link ChunkLightPropagator} class spreads light through every section of a chunk at once.
 * <p>
 * A propagation which stops at a section border produces a visible seam every sixteen blocks,
 * because a light source near the border lights its own section and nothing beyond it. This class
 * therefore treats the sections of a chunk as one column and lets the search cross their borders.
 * </p>
 * <p>
 * The search is the same breadth-first pass {@link LightPropagator} performs, extended by the
 * vertical axis. Since a level drops by exactly one per block, every position is still reached with
 * its final level on the first visit.
 * </p>
 * <p>
 * An instance keeps its buffers for the largest column it has seen and reuses them, so repeated
 * runs allocate nothing beyond their result. It is reusable but confined to a single thread.
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
public final class ChunkLightPropagator {

    private static final BlockFace[] FACES = BlockFace.values();
    private static final int MASK = LightNibbles.DIMENSION - 1;

    private byte[] levels;
    private int[] queue;

    /**
     * Creates a new propagator without any buffer. The buffers are sized on the first run.
     */
    public ChunkLightPropagator() {
        this.levels = new byte[0];
        this.queue = new int[0];
    }

    /**
     * Calculates the light of every section of a chunk.
     * The sections are expected in the order they are stacked, starting with the lowest one.
     *
     * @param sections the light properties of every section of the chunk
     * @return the calculated light of every section, in the order the sections were given
     * @throws IllegalArgumentException if the chunk holds no section
     */
    public List<LightNibbles> propagate(List<SectionOpacity> sections) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("A chunk has to hold at least one section");
        }

        int height = sections.size() * LightNibbles.DIMENSION;
        int blockCount = height * LightNibbles.DIMENSION * LightNibbles.DIMENSION;
        ensureCapacity(blockCount);

        java.util.Arrays.fill(this.levels, 0, blockCount, (byte) 0);
        int tail = seed(sections, height);
        int head = 0;

        while (head < tail) {
            int index = this.queue[head++];
            int level = this.levels[index];

            if (level <= 1) {
                continue;
            }

            int x = index & MASK;
            int z = (index >> 4) & MASK;
            int y = index >> 8;
            int next = level - 1;

            for (BlockFace face : FACES) {
                int neighbourX = x + face.offsetX();
                int neighbourY = y + face.offsetY();
                int neighbourZ = z + face.offsetZ();

                if (isOutside(neighbourX, neighbourY, neighbourZ, height)) {
                    continue;
                }
                if (blocksFace(sections, neighbourX, neighbourY, neighbourZ, face.opposite())) {
                    continue;
                }

                int neighbourIndex = index(neighbourX, neighbourY, neighbourZ);

                if (this.levels[neighbourIndex] >= next) {
                    continue;
                }
                this.levels[neighbourIndex] = (byte) next;
                this.queue[tail++] = neighbourIndex;
            }
        }
        return collect(sections.size());
    }

    /**
     * Grows the buffers if the given column needs more room than the previous one.
     *
     * @param blockCount the amount of blocks the column holds
     */
    private void ensureCapacity(int blockCount) {
        if (this.levels.length < blockCount) {
            this.levels = new byte[blockCount];
            this.queue = new int[blockCount];
        }
    }

    /**
     * Puts every emitting block of the column into the queue.
     *
     * @param sections the light properties of every section
     * @param height   the amount of blocks the column spans vertically
     * @return the amount of queued positions
     */
    private int seed(List<SectionOpacity> sections, int height) {
        int tail = 0;

        for (int y = 0; y < height; y++) {
            SectionOpacity section = sections.get(y >> 4);

            if (!section.hasEmission()) {
                continue;
            }
            int localY = y & MASK;

            for (int z = 0; z < LightNibbles.DIMENSION; z++) {
                for (int x = 0; x < LightNibbles.DIMENSION; x++) {
                    int emission = section.emission(x, localY, z);

                    if (emission <= 0) {
                        continue;
                    }
                    int index = index(x, y, z);
                    this.levels[index] = (byte) emission;
                    this.queue[tail++] = index;
                }
            }
        }
        return tail;
    }

    /**
     * Checks whether light cannot enter the given position through the given face.
     *
     * @param sections the light properties of every section
     * @param x        the x coordinate inside the chunk
     * @param y        the y coordinate inside the column
     * @param z        the z coordinate inside the chunk
     * @param face     the face light would enter through
     * @return true if light cannot pass the face, otherwise false
     */
    private static boolean blocksFace(List<SectionOpacity> sections, int x, int y, int z, BlockFace face) {
        return sections.get(y >> 4).blocksFace(x, y & MASK, z, face);
    }

    /**
     * Transfers the calculated levels into one light section per section of the chunk.
     *
     * @param sectionCount the amount of sections the chunk holds
     * @return the calculated light of every section
     */
    private List<LightNibbles> collect(int sectionCount) {
        List<LightNibbles> result = new ArrayList<>(sectionCount);

        for (int section = 0; section < sectionCount; section++) {
            int base = section * LightNibbles.DIMENSION;
            result.add(collectSection(base));
        }
        return result;
    }

    /**
     * Transfers the levels of a single section into a light section.
     *
     * @param baseY the lowest y coordinate of the section inside the column
     * @return the light of the section
     */
    private LightNibbles collectSection(int baseY) {
        byte first = this.levels[index(0, baseY, 0)];
        boolean uniform = true;

        outer:
        for (int y = 0; y < LightNibbles.DIMENSION; y++) {
            for (int z = 0; z < LightNibbles.DIMENSION; z++) {
                for (int x = 0; x < LightNibbles.DIMENSION; x++) {
                    if (this.levels[index(x, baseY + y, z)] != first) {
                        uniform = false;
                        break outer;
                    }
                }
            }
        }

        if (uniform) {
            return LightNibbles.uniform(first);
        }

        LightNibbles light = LightNibbles.uniform(0);

        for (int y = 0; y < LightNibbles.DIMENSION; y++) {
            for (int z = 0; z < LightNibbles.DIMENSION; z++) {
                for (int x = 0; x < LightNibbles.DIMENSION; x++) {
                    int level = this.levels[index(x, baseY + y, z)];

                    if (level != 0) {
                        light.set(x, y, z, level);
                    }
                }
            }
        }
        return light;
    }

    /**
     * Checks whether the given position lies outside of the column.
     *
     * @param x      the x coordinate to check
     * @param y      the y coordinate to check
     * @param z      the z coordinate to check
     * @param height the amount of blocks the column spans vertically
     * @return true if the position is outside of the column, otherwise false
     */
    private static boolean isOutside(int x, int y, int z, int height) {
        return (x | y | z) < 0 || x >= LightNibbles.DIMENSION || z >= LightNibbles.DIMENSION || y >= height;
    }

    /**
     * Calculates the index of a block inside the column.
     *
     * @param x the x coordinate inside the chunk
     * @param y the y coordinate inside the column
     * @param z the z coordinate inside the chunk
     * @return the index of the block
     */
    private static int index(int x, int y, int z) {
        return (y << 8) | (z << 4) | x;
    }
}
