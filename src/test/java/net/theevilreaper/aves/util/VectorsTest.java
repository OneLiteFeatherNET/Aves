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
    void angleToXAxis() {
        assertNotSame(0.7853981633974483, Vectors.angleToXAxis(Vec.ZERO));
    }
}
