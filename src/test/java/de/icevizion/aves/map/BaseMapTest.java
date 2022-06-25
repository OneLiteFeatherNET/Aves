package de.icevizion.aves.map;

import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BaseMapTest {

    private BaseMap firstMap;
    private BaseMap secondMap;

    private String[] builders;

    @BeforeAll
    void init() {
        this.builders = new String[]{"theEvilReaper, Tresson", "SeelenRetterin"};
        this.firstMap = BaseMap.of("Test");
        this.secondMap = BaseMap.of("Test", new Pos(120,51,23), builders[0], builders[1]);
    }

    @Test
    void testMapIsSame() {
        assertEquals(this.firstMap, this.secondMap);
    }

    @Test
    void testHasSpawn() {
        assertNotNull(this.secondMap.getSpawn());
    }

    @Test
    void testOriginPos() {
        assertFalse(this.secondMap.getSpawn() != null && this.secondMap.getSpawn().samePoint(Pos.ZERO));
    }

    @Test
    void testEmptyName() {
        var exception = assertThrows(IllegalArgumentException.class, () -> this.firstMap.setName(""));
        assertEquals("The name can not be empty", exception.getMessage());
    }

    @Test
    void testEmptyNameConstructor() {
        var exception = assertThrows(IllegalArgumentException.class, () -> BaseMap.of(""));
        assertEquals("The name can not be null or empty", exception.getMessage());
    }

    @Test
    void testSetBuilders() {
        var builders = new String[]{"theEvilReaper, SeelenRetterin"};
        assertFalse(Arrays.equals(this.secondMap.getBuilders(), builders));
    }

    @Test
    void testBuilder() {
        this.firstMap.setBuilders(this.builders);
        assertArrayEquals(this.firstMap.getBuilders(), this.builders);
    }

    @Test
    void testNoSpawn() {
        assertNull(this.firstMap.getSpawn());
    }
}