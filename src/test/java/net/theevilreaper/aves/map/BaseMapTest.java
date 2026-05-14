package net.theevilreaper.aves.map;

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
        this.builders = new String[]{"theEvilReaper", "Tresson"};

        this.firstMap = new BaseMap("Test", null);
        this.secondMap = new BaseMap(
                "Test",
                new Pos(120, 51, 23),
                builders
        );
    }

    @Test
    void testName() {
        assertEquals("Test", firstMap.name());
    }

    @Test
    void testSpawnNullable() {
        assertNull(firstMap.spawn());
        assertNotNull(secondMap.spawn());
    }

    @Test
    void testDifferentInstances() {
        assertNotEquals(firstMap, secondMap);
    }

    @Test
    void testSpawnValue() {
        assertEquals(new Pos(120, 51, 23), secondMap.spawn());
    }

    @Test
    void testBuildersStored() {
        assertArrayEquals(builders, secondMap.builders());
    }

    @Test
    void testGetSpawnOrDefault() {
        BaseMap map = new BaseMap("Test", null);

        Pos fallback = new Pos(1, 2, 3);
        assertEquals(fallback, map.getSpawnOrDefault(fallback));
    }

    @Test
    void testEqualsDifferentName() {
        BaseMap a = new BaseMap("A", null);
        BaseMap b = new BaseMap("B", null);

        assertNotEquals(a, b);
    }
}