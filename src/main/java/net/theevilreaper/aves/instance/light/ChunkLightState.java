package net.theevilreaper.aves.instance.light;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link ChunkLightState} class holds the calculated light of a chunk and updates it when a
 * single block changed, without recalculating the whole chunk.
 * <p>
 * Adding brightness is easy: the new light spreads and never has to take anything back. Removing it
 * is the hard case and the reason this class exists. When a light source disappears, the brightness
 * it had spread is still stored in every block around it, and simply spreading again would keep
 * that glow forever. The update therefore runs in two passes: the first retracts every level which
 * originated from the changed position, collecting the still valid levels it meets at the edge, and
 * the second spreads those back in.
 * </p>
 * <p>
 * Instances are not thread safe. Keep one per chunk and use it from one thread at a time.
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
public final class ChunkLightState {

    private static final BlockFace[] FACES = BlockFace.values();
    private static final int MASK = LightNibbles.DIMENSION - 1;

    private final byte[] levels;
    private final int sectionCount;
    private final int height;
    private final boolean sky;

    private final int[] removalQueue;
    private final byte[] removalLevels;
    private final int[] additionQueue;

    /**
     * Creates a new state from the given levels.
     *
     * @param levels       the level of every block of the column
     * @param sectionCount the amount of sections the chunk holds
     * @param sky          whether the state holds sky light
     */
    private ChunkLightState(byte[] levels, int sectionCount, boolean sky) {
        this.levels = levels;
        this.sectionCount = sectionCount;
        this.height = sectionCount * LightNibbles.DIMENSION;
        this.sky = sky;
        this.removalQueue = new int[levels.length];
        this.removalLevels = new byte[levels.length];
        this.additionQueue = new int[levels.length];
    }

    /**
     * Calculates the block light of a chunk and keeps it for later updates.
     *
     * @param sections the light properties of every section of the chunk
     * @return the created state
     */
    public static ChunkLightState blockLight(List<SectionOpacity> sections) {
        return of(sections, new ChunkLightPropagator().propagate(sections), false);
    }

    /**
     * Calculates the sky light of a chunk and keeps it for later updates.
     *
     * @param sections the light properties of every section of the chunk
     * @return the created state
     */
    public static ChunkLightState skyLight(List<SectionOpacity> sections) {
        return of(sections, new ChunkLightPropagator().propagateSky(sections), true);
    }

    /**
     * Builds a state from an already calculated light.
     *
     * @param sections the light properties of every section of the chunk
     * @param light    the calculated light of every section
     * @param sky      whether the light is sky light
     * @return the created state
     */
    private static ChunkLightState of(List<SectionOpacity> sections, List<LightNibbles> light, boolean sky) {
        int sectionCount = sections.size();
        byte[] levels = new byte[sectionCount * LightNibbles.BLOCK_COUNT];

        for (int section = 0; section < sectionCount; section++) {
            LightNibbles nibbles = light.get(section);
            int base = section * LightNibbles.DIMENSION;

            for (int y = 0; y < LightNibbles.DIMENSION; y++) {
                for (int z = 0; z < LightNibbles.DIMENSION; z++) {
                    for (int x = 0; x < LightNibbles.DIMENSION; x++) {
                        levels[index(x, base + y, z)] = (byte) nibbles.get(x, y, z);
                    }
                }
            }
        }
        return new ChunkLightState(levels, sectionCount, sky);
    }

    /**
     * Returns the level which is currently stored for the given position.
     *
     * @param x the x coordinate inside the chunk
     * @param y the y coordinate inside the column
     * @param z the z coordinate inside the chunk
     * @return the stored level of the position
     */
    @Contract(pure = true)
    public int get(int x, int y, int z) {
        return this.levels[index(x, y, z)];
    }

    /**
     * Updates the light after the block at the given position changed.
     *
     * @param sections the light properties of every section, reflecting the change
     * @param x        the x coordinate inside the chunk
     * @param y        the y coordinate inside the column
     * @param z        the z coordinate inside the chunk
     */
    public void update(List<SectionOpacity> sections, int x, int y, int z) {
        int start = index(x, y, z);
        int additions = retract(sections, start);
        additions = seedEmission(sections, additions);
        spread(sections, additions);
    }

    /**
     * Retracts every level which originated from the changed position.
     * <p>
     * A neighbour which is darker than the level being removed can only have received its light
     * from it, so it is cleared as well. A neighbour which is as bright or brighter has another
     * origin and becomes a starting point for the second pass instead.
     * </p>
     *
     * @param sections the light properties of every section
     * @param start    the index of the changed position
     * @return the amount of positions which were queued for the second pass
     */
    private int retract(List<SectionOpacity> sections, int start) {
        int removalTail = 0;
        int additionTail = 0;

        this.removalQueue[removalTail] = start;
        this.removalLevels[removalTail++] = this.levels[start];
        this.levels[start] = 0;

        for (int head = 0; head < removalTail; head++) {
            int index = this.removalQueue[head];
            int removed = this.removalLevels[head];

            if (removed == 0) {
                continue;
            }

            int x = index & MASK;
            int z = (index >> 4) & MASK;
            int y = index >> 8;

            for (BlockFace face : FACES) {
                int neighbourX = x + face.offsetX();
                int neighbourY = y + face.offsetY();
                int neighbourZ = z + face.offsetZ();

                if (isOutside(neighbourX, neighbourY, neighbourZ)) {
                    continue;
                }

                int neighbourIndex = index(neighbourX, neighbourY, neighbourZ);
                int level = this.levels[neighbourIndex];

                if (level == 0) {
                    continue;
                }
                if (level < removed) {
                    this.removalQueue[removalTail] = neighbourIndex;
                    this.removalLevels[removalTail++] = (byte) level;
                    this.levels[neighbourIndex] = 0;
                    continue;
                }
                this.additionQueue[additionTail++] = neighbourIndex;
            }
        }
        return additionTail;
    }

    /**
     * Adds every position which produces light on its own to the second pass.
     * For sky light the open columns are seeded instead, because its origin is the sky and not a
     * block.
     *
     * @param sections the light properties of every section
     * @param queued   the amount of positions which are already queued
     * @return the amount of queued positions
     */
    private int seedEmission(List<SectionOpacity> sections, int queued) {
        int tail = queued;

        if (this.sky) {
            for (int z = 0; z < LightNibbles.DIMENSION; z++) {
                for (int x = 0; x < LightNibbles.DIMENSION; x++) {
                    for (int y = this.height - 1; y >= 0; y--) {
                        if (blocksFace(sections, x, y, z, BlockFace.TOP)) {
                            break;
                        }
                        int index = index(x, y, z);

                        if (this.levels[index] < LightNibbles.MAX_LEVEL) {
                            this.levels[index] = LightNibbles.MAX_LEVEL;
                        }
                        this.additionQueue[tail++] = index;
                    }
                }
            }
            return tail;
        }

        for (int y = 0; y < this.height; y++) {
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

                    if (this.levels[index] < emission) {
                        this.levels[index] = (byte) emission;
                    }
                    this.additionQueue[tail++] = index;
                }
            }
        }
        return tail;
    }

    /**
     * Spreads the queued levels back into the retracted area.
     *
     * @param sections the light properties of every section
     * @param queued   the amount of queued positions
     */
    private void spread(List<SectionOpacity> sections, int queued) {
        int tail = queued;

        for (int head = 0; head < tail; head++) {
            int index = this.additionQueue[head];
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

                if (isOutside(neighbourX, neighbourY, neighbourZ)) {
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
                this.additionQueue[tail++] = neighbourIndex;
            }
        }
    }

    /**
     * Returns the light levels along one horizontal border of the chunk.
     * <p>
     * The result is read by the neighbouring chunk to continue the light across the border. It is
     * ordered by height first and by the remaining horizontal axis second.
     * </p>
     *
     * @param face the border to read
     * @return the level of every block along the border
     * @throws IllegalArgumentException if the given face is not horizontal
     */
    @Contract(pure = true)
    public byte[] border(BlockFace face) {
        checkHorizontal(face);
        byte[] border = new byte[this.height * LightNibbles.DIMENSION];

        for (int y = 0; y < this.height; y++) {
            for (int offset = 0; offset < LightNibbles.DIMENSION; offset++) {
                border[y * LightNibbles.DIMENSION + offset] = this.levels[borderIndex(face, y, offset)];
            }
        }
        return border;
    }

    /**
     * Feeds the light of a neighbouring chunk into this one.
     * <p>
     * Without this a light source close to the edge of a chunk lights its own chunk and stops
     * abruptly at the border, which shows up as a straight dark line every sixteen blocks. Each
     * level of the neighbour arrives one level weaker, exactly as if the two chunks had been
     * calculated together.
     * </p>
     *
     * @param sections the light properties of every section of this chunk
     * @param face     the border the light enters through
     * @param border   the levels along the matching border of the neighbour
     * @throws IllegalArgumentException if the face is not horizontal or the border has the wrong size
     */
    public void injectBorder(List<SectionOpacity> sections, BlockFace face, byte[] border) {
        checkHorizontal(face);

        if (border.length != this.height * LightNibbles.DIMENSION) {
            throw new IllegalArgumentException(
                    "The border of this chunk holds " + (this.height * LightNibbles.DIMENSION)
                            + " levels but the given one holds " + border.length
            );
        }

        int tail = 0;

        for (int y = 0; y < this.height; y++) {
            for (int offset = 0; offset < LightNibbles.DIMENSION; offset++) {
                int incoming = border[y * LightNibbles.DIMENSION + offset] - 1;

                if (incoming <= 0) {
                    continue;
                }

                int index = borderIndex(face, y, offset);

                if (this.levels[index] >= incoming) {
                    continue;
                }
                // The light enters through the face that lies towards the neighbour.
                if (blocksFace(sections, index & MASK, index >> 8, (index >> 4) & MASK, face)) {
                    continue;
                }
                this.levels[index] = (byte) incoming;
                this.additionQueue[tail++] = index;
            }
        }
        spread(sections, tail);
    }

    /**
     * Calculates the index of a block which lies on the given border.
     *
     * @param face   the border the block lies on
     * @param y      the y coordinate inside the column
     * @param offset the position along the remaining horizontal axis
     * @return the index of the block
     */
    @Contract(pure = true)
    private static int borderIndex(BlockFace face, int y, int offset) {
        return switch (face) {
            case WEST -> index(0, y, offset);
            case EAST -> index(LightNibbles.DIMENSION - 1, y, offset);
            case NORTH -> index(offset, y, 0);
            case SOUTH -> index(offset, y, LightNibbles.DIMENSION - 1);
            default -> throw new IllegalArgumentException("The face " + face + " is not horizontal");
        };
    }

    /**
     * Verifies that the given face describes a horizontal border.
     *
     * @param face the face to check
     * @throws IllegalArgumentException if the face is not horizontal
     */
    private static void checkHorizontal(BlockFace face) {
        if (face == BlockFace.TOP || face == BlockFace.BOTTOM) {
            throw new IllegalArgumentException(
                    "Only a horizontal border is shared between two chunks but " + face + " was given"
            );
        }
    }

    /**
     * Returns the stored light as one light section per section of the chunk.
     *
     * @return the light of every section
     */
    @Contract(pure = true)
    public List<LightNibbles> toSections() {
        List<LightNibbles> result = new ArrayList<>(this.sectionCount);

        for (int section = 0; section < this.sectionCount; section++) {
            int base = section * LightNibbles.DIMENSION;
            LightNibbles nibbles = LightNibbles.uniform(0);
            boolean uniform = true;
            int first = this.levels[index(0, base, 0)];

            for (int y = 0; y < LightNibbles.DIMENSION; y++) {
                for (int z = 0; z < LightNibbles.DIMENSION; z++) {
                    for (int x = 0; x < LightNibbles.DIMENSION; x++) {
                        int level = this.levels[index(x, base + y, z)];

                        if (level != first) {
                            uniform = false;
                        }
                        if (level != 0) {
                            nibbles.set(x, y, z, level);
                        }
                    }
                }
            }
            result.add(uniform ? LightNibbles.uniform(first) : nibbles);
        }
        return result;
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
     * Checks whether the given position lies outside of the column.
     *
     * @param x the x coordinate to check
     * @param y the y coordinate to check
     * @param z the z coordinate to check
     * @return true if the position is outside of the column, otherwise false
     */
    private boolean isOutside(int x, int y, int z) {
        return (x | y | z) < 0 || x >= LightNibbles.DIMENSION || z >= LightNibbles.DIMENSION || y >= this.height;
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
