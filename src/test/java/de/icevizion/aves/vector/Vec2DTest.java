package de.icevizion.aves.vector;

import de.icevizion.aves.util.vector.Vec2D;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Vec2DTest {

    private Vec2D vector1;

    private Vec2D vector2;

    @BeforeAll
    void init() {
        this.vector1 = new Vec2D(2, 5);
        this.vector2 = new Vec2D(-6, -6);
    }

    @Test
    void testOtherConstructorWithPoint() {
        Vec2D vec = new Vec2D(new Pos(2, 5, 3, 2f, 1f));
        assertNotNull(vec);
        assertEquals(2, vec.x());
        assertNotEquals(5.0, vec.y());
    }

    @Test
    void testOtherConstructorWithVec() {
        Vec2D vec = new Vec2D(new Vec(2.0, 10, 3));
        assertNotNull(vec);
        assertEquals(2.0, vec.x());
        assertNotEquals(5.0, vec.y());
    }

    @Test
    void testAddVecToVec() {
        Vec2D result = new Vec2D(3, 5);
        assertEquals(result, this.vector1.add(Vec2D.UNIT_X));
    }

    @Test
    void testAddVecToVecNeg() {
        Vec2D result = new Vec2D(- 5, -6);
        assertEquals(result, this.vector2.add(Vec2D.UNIT_X));
    }

    @Test
    void testAddValuesToVec() {
        Vec2D result = new Vec2D(4, 8);
        assertEquals(result, this.vector1.add(2, 3));
    }

    @Test
    void testAddValuesToVecNeg() {
        Vec2D result = new Vec2D(-4, -3);
        assertEquals(result, this.vector2.add(2, 3));
    }

    @Test
    void testRemoveVecToVec() {
        Vec2D result = new Vec2D(0, -1);
        assertEquals(result, this.vector1.sub(2, 6));
    }

    @Test
    void testRemoveVec() {
        assertEquals(Vec2D.UNIT_Y, new Vec2D(1,1).sub(Vec2D.UNIT_X));
    }

    @Test
    void testNegation() {
        assertEquals(new Vec2D(-2, -5), this.vector1.neg());
    }

    @Test
    void testLength() {
        assertEquals(Math.sqrt(29), this.vector1.length());
    }

    @Test
    void testLengthSquared() {
        assertEquals(72, this.vector2.lengthSquared());
    }

    @Test
    void testScalarMul() {
        var vector = this.vector2.scalarMul(1);
        assertEquals(new Vec2D(-6, -6), vector);
    }
}
