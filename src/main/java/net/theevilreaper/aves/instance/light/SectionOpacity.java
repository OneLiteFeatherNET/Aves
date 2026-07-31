package net.theevilreaper.aves.instance.light;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link SectionOpacity} class holds the light properties of every block of a section in a form
 * the propagation can read without touching a registry.
 * <p>
 * Resolving the properties of a block is the dominant cost of a light propagation, because a
 * breadth-first search visits the same block from up to six directions and would otherwise resolve
 * it again every time. This class resolves every distinct block state once when the table is built
 * and answers from two arrays afterwards.
 * </p>
 * <p>
 * The occlusion of a block is stored per face. A block which occludes only some of its faces, such
 * as a slab or a stair, is common enough that a single flag per block would produce visibly wrong
 * light.
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
public final class SectionOpacity {

    private static final BlockFace[] FACES = BlockFace.values();

    private final byte[] occlusion;
    private final byte[] emission;
    private final boolean hasEmission;
    private final boolean fullyTransparent;

    /**
     * Creates a new table from the given values.
     *
     * @param occlusion        the occluded faces of every block as a bit mask
     * @param emission         the emitted light level of every block
     * @param hasEmission      whether any block of the section emits light
     * @param fullyTransparent whether no block of the section occludes any face
     */
    private SectionOpacity(byte[] occlusion, byte[] emission, boolean hasEmission, boolean fullyTransparent) {
        this.occlusion = occlusion;
        this.emission = emission;
        this.hasEmission = hasEmission;
        this.fullyTransparent = fullyTransparent;
    }

    /**
     * Builds the table for a section from the state ids of its blocks.
     * Every distinct state id is resolved exactly once.
     *
     * @param stateIds the state id of every block of the section
     * @param source   the source which describes the light properties of a block
     * @return the created table
     * @throws IllegalArgumentException if the given array does not cover the whole section
     */
    public static SectionOpacity of(int[] stateIds, BlockLightSource source) {
        if (stateIds.length != LightNibbles.BLOCK_COUNT) {
            throw new IllegalArgumentException(
                    "A section holds " + LightNibbles.BLOCK_COUNT + " blocks but the given array holds " + stateIds.length
            );
        }

        Map<Integer, byte[]> resolved = new HashMap<>();
        byte[] occlusion = new byte[stateIds.length];
        byte[] emission = new byte[stateIds.length];
        boolean anyEmission = false;
        boolean anyOcclusion = false;

        for (int index = 0; index < stateIds.length; index++) {
            byte[] properties = resolved.computeIfAbsent(stateIds[index], stateId -> resolve(stateId, source));
            occlusion[index] = properties[0];
            emission[index] = properties[1];
            anyEmission |= properties[1] != 0;
            anyOcclusion |= properties[0] != 0;
        }
        return new SectionOpacity(occlusion, emission, anyEmission, !anyOcclusion);
    }

    /**
     * Resolves the light properties of a single block state.
     *
     * @param stateId the state id to resolve
     * @param source  the source which describes the light properties of a block
     * @return the occlusion mask and the emission of the state
     */
    private static byte[] resolve(int stateId, BlockLightSource source) {
        int mask = 0;

        for (BlockFace face : FACES) {
            if (source.blocksFace(stateId, face)) {
                mask |= 1 << face.ordinal();
            }
        }
        return new byte[]{(byte) mask, (byte) source.emission(stateId)};
    }

    /**
     * Checks whether light is unable to pass the given face of the block at the given position.
     *
     * @param x    the x coordinate inside the section
     * @param y    the y coordinate inside the section
     * @param z    the z coordinate inside the section
     * @param face the face to check
     * @return true if light cannot pass the face, otherwise false
     */
    @Contract(pure = true)
    public boolean blocksFace(int x, int y, int z, BlockFace face) {
        return (this.occlusion[index(x, y, z)] & (1 << face.ordinal())) != 0;
    }

    /**
     * Returns the amount of light the block at the given position emits.
     *
     * @param x the x coordinate inside the section
     * @param y the y coordinate inside the section
     * @param z the z coordinate inside the section
     * @return the emitted light level of the block
     */
    @Contract(pure = true)
    public int emission(int x, int y, int z) {
        return this.emission[index(x, y, z)];
    }

    /**
     * Checks whether any block of the section emits light.
     * A section without an emitting block needs no block light propagation at all.
     *
     * @return true if a block of the section emits light, otherwise false
     */
    @Contract(pure = true)
    public boolean hasEmission() {
        return this.hasEmission;
    }

    /**
     * Checks whether no block of the section occludes any face.
     * Light travels through such a section without any obstacle.
     *
     * @return true if the section occludes nothing, otherwise false
     */
    @Contract(pure = true)
    public boolean isFullyTransparent() {
        return this.fullyTransparent;
    }

    /**
     * Calculates the index of a block inside the section.
     *
     * @param x the x coordinate inside the section
     * @param y the y coordinate inside the section
     * @param z the z coordinate inside the section
     * @return the index of the block
     */
    @Contract(pure = true)
    private static int index(int x, int y, int z) {
        return (y << 8) | (z << 4) | x;
    }
}
