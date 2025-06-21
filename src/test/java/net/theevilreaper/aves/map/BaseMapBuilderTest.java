package net.theevilreaper.aves.map;

import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseMapBuilderTest {

    @Test
    void testMapCreationViaBuilder() {
        BaseMapBuilder builder = BaseMap.builder();

        assertNotNull(builder);

        builder.name("TestMap")
                .builders("Author1", "Author2")
                .spawn(new Pos(0, 64, 0));

        BaseMap map = builder.build();

        assertNotNull(map);

        assertEquals("TestMap", map.getName());
        assertArrayEquals(new String[]{"Author1", "Author2"}, map.getBuilders());
        assertEquals(new Pos(0, 64, 0), map.getSpawn());
    }

    @Test
    void testMapUpdateWithExistingMap() {
        BaseMapBuilder builder = BaseMap.builder();

        builder.name("TestMap")
                .builders("Author1")
                .spawn(new Pos(0, 64, 0));
        BaseMap map = builder.build();

        assertNotNull(map);

        BaseMapBuilder builder1 = BaseMap.builder(map);

        assertNotNull(builder1);

        builder1.builder("Author2")
                .spawn(new Pos(1, 65, 1));

        BaseMap updatedMap = builder1.build();

        assertNotNull(updatedMap);
        assertEquals("TestMap", updatedMap.getName());
        assertArrayEquals(new String[]{"Author1", "Author2"}, updatedMap.getBuilders());
        assertEquals(new Pos(1, 65, 1), updatedMap.getSpawn());
        assertNotEquals(map.getSpawn(), updatedMap.getSpawn());
    }
}