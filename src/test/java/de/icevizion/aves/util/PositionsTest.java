package de.icevizion.aves.util;

import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PositionsTest {
    
    private Pos originPoint;
    
    @BeforeAll
    void init() {
        this.originPoint = new Pos(0,0,0);
    }

    @Test
    void centerPos3D() {
        var resultPos = new Pos(0.5, 0.5, 0.5);
        assertEquals(resultPos, Positions.centerPos3D(this.originPoint));
    }

    @Test
    void testCenter2D() {
        var result = new Pos(0.5, 0, 0.5);
        assertEquals(result, Positions.centerPos2D(this.originPoint));
    }

    @Test
    void testYaw() {
        var yaw = Positions.getYaw(12, 10);
        assertEquals(0.876058042049408, yaw);
    }

    @Test
    void testLengthSquaredPos() {
        assertNotSame(12.4, Positions.lengthSquaredPos(this.originPoint));
    }
}