package net.theevilreaper.aves.instance.light;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the precomputed opacity table of a section. Resolving the properties of a block for every
 * visited neighbour is the dominant cost of a light propagation, so they are looked up once per
 * block and read from a table afterwards.
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.16.0
 */
class SectionOpacityTest {

    private static final int AIR = 0;
    private static final int STONE = 1;
    private static final int GLOWSTONE = 2;
    private static final int SLAB = 3;

    /**
     * A source which describes four blocks without touching any registry.
     * The slab blocks the downwards face only, which is what makes a single occlusion flag wrong.
     */
    private static final BlockLightSource SOURCE = new BlockLightSource() {

        @Override
        public int emission(int stateId) {
            return stateId == GLOWSTONE ? 15 : 0;
        }

        @Override
        public boolean blocksFace(int stateId, BlockFace face) {
            return switch (stateId) {
                case STONE -> true;
                case SLAB -> face == BlockFace.BOTTOM;
                default -> false;
            };
        }
    };

    /**
     * Builds a section in which every block holds the given state id.
     *
     * @param stateId the state id of every block
     * @return the created table
     */
    private static SectionOpacity uniformSection(int stateId) {
        int[] states = new int[LightNibbles.BLOCK_COUNT];
        java.util.Arrays.fill(states, stateId);
        return SectionOpacity.of(states, SOURCE);
    }

    @Test
    void testATransparentBlockBlocksNoFace() {
        SectionOpacity opacity = uniformSection(AIR);

        for (BlockFace face : BlockFace.values()) {
            assertFalse(opacity.blocksFace(0, 0, 0, face), "air must not block " + face);
        }
    }

    @Test
    void testAnOpaqueBlockBlocksEveryFace() {
        SectionOpacity opacity = uniformSection(STONE);

        for (BlockFace face : BlockFace.values()) {
            assertTrue(opacity.blocksFace(5, 5, 5, face), "stone must block " + face);
        }
    }

    @Test
    void testADirectionalBlockOnlyBlocksItsOwnFaces() {
        // Slabs, stairs, snow and farmland occlude some faces and not others. A table which stores
        // a single flag per block would answer this wrongly for roughly one in seven block types.
        SectionOpacity opacity = uniformSection(SLAB);

        assertTrue(opacity.blocksFace(1, 1, 1, BlockFace.BOTTOM));
        assertFalse(opacity.blocksFace(1, 1, 1, BlockFace.TOP));
        assertFalse(opacity.blocksFace(1, 1, 1, BlockFace.NORTH));
    }

    @Test
    void testTheEmissionOfABlockIsKept() {
        SectionOpacity opacity = uniformSection(GLOWSTONE);

        assertEquals(15, opacity.emission(3, 4, 5));
    }

    @Test
    void testABlockWithoutEmissionReportsZero() {
        assertEquals(0, uniformSection(STONE).emission(0, 0, 0));
    }

    @Test
    void testASectionWithoutAnyEmissionIsReported() {
        assertFalse(uniformSection(STONE).hasEmission());
    }

    @Test
    void testASectionWithAnEmittingBlockIsReported() {
        int[] states = new int[LightNibbles.BLOCK_COUNT];
        states[42] = GLOWSTONE;

        assertTrue(SectionOpacity.of(states, SOURCE).hasEmission());
    }

    @Test
    void testAFullyTransparentSectionIsReported() {
        assertTrue(uniformSection(AIR).isFullyTransparent());
        assertFalse(uniformSection(STONE).isFullyTransparent());
    }

    @Test
    void testMixedBlocksAreResolvedPerPosition() {
        int[] states = new int[LightNibbles.BLOCK_COUNT];
        states[index(2, 3, 4)] = STONE;
        states[index(2, 3, 5)] = GLOWSTONE;
        SectionOpacity opacity = SectionOpacity.of(states, SOURCE);

        assertTrue(opacity.blocksFace(2, 3, 4, BlockFace.TOP));
        assertFalse(opacity.blocksFace(2, 3, 5, BlockFace.TOP));
        assertEquals(15, opacity.emission(2, 3, 5));
        assertEquals(0, opacity.emission(2, 3, 4));
    }

    @Test
    void testEveryDistinctStateIsResolvedOnlyOnce() {
        int[] states = new int[LightNibbles.BLOCK_COUNT];
        java.util.Arrays.fill(states, STONE);
        CountingSource counting = new CountingSource();

        SectionOpacity.of(states, counting);

        assertEquals(1, counting.resolved, "4096 blocks of one state must cost one lookup");
    }

    @Test
    void testTheStateArrayMustCoverTheWholeSection() {
        assertThrows(IllegalArgumentException.class, () -> SectionOpacity.of(new int[10], SOURCE));
    }

    /**
     * Calculates the index of a block inside a section.
     *
     * @param x the x coordinate inside the section
     * @param y the y coordinate inside the section
     * @param z the z coordinate inside the section
     * @return the index of the block
     */
    private static int index(int x, int y, int z) {
        return (y << 8) | (z << 4) | x;
    }

    /**
     * A source which counts how often a distinct state was resolved.
     */
    private static final class CountingSource implements BlockLightSource {

        private int resolved;

        @Override
        public int emission(int stateId) {
            this.resolved++;
            return 0;
        }

        @Override
        public boolean blocksFace(int stateId, BlockFace face) {
            return true;
        }
    }
}
