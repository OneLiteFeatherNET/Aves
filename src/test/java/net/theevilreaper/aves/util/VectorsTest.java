package net.theevilreaper.aves.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorsTest {

    @Test
    void testRandomVector() {
        assertNotEquals(Vec.ZERO, Vectors.getRandomVector());
    }

    @Test
    void testBackVector() {
        assertNotEquals(Vec.ZERO, Vectors.getBackVector(Pos.ZERO));
    }

    @Test
    void testGetRandomCircleVector() {
        assertNotEquals(Vec.ONE, Vectors.getRandomCircleVector());
    }

    @Test
    void testAngleToXAxis() {
        assertEquals(0.0, Vectors.angleToXAxis(new Vec(1, 0, 0)), 1e-6, "Vector along positive X axis should have angle 0");
        assertEquals(Math.PI / 2, Vectors.angleToXAxis(new Vec(0, 1, 0)), 1e-6, "Vector along positive Y axis should have angle PI/2");
        assertEquals(Math.PI / 4, Vectors.angleToXAxis(new Vec(1, 1, 0)), 1e-6, "Vector (1, 1) should have angle PI/4");
    }
}
