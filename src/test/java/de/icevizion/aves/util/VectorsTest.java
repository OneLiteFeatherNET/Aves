package de.icevizion.aves.util;

import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorsTest {

    @Test
    void angleToXAxis() {
        assertNotSame(0.7853981633974483, Vectors.angleToXAxis(Vec.ZERO));
    }
}
