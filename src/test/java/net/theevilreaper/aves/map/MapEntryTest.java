package net.theevilreaper.aves.map;

import net.theevilreaper.aves.FileTestBase;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapEntryTest extends FileTestBase {

    @Test
    void testCreationOnNonDirectory() {
        Path path = tempDir.resolve("test.json");
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> MapEntry.of(path),
                "The given path must be a directory"
        );
    }

    @Test
    void testMapEntryFileCreation() {
        MapEntry mapEntry = MapEntry.of(tempDir);
        assertNotNull(mapEntry);
        assertNull(mapEntry.getMapFile());
        assertFalse(mapEntry.hasMapFile());

        mapEntry.createFile();

        assertNotNull(mapEntry.getMapFile());
        assertTrue(mapEntry.hasMapFile());
    }

    @Test
    void testMapEntryWithStandardFileEnding() {
        MapEntry standardEntry = MapEntry.of(tempDir);
        assertNotNull(standardEntry);
        assertEquals(tempDir, standardEntry.getDirectoryRoot());
        assertTrue(standardEntry.hasStandardEnding());
        assertNull(standardEntry.getMapFile());
        standardEntry.createFile();
        assertTrue(standardEntry.hasMapFile());
        assertNotNull(standardEntry.getMapFile());
    }

    @Test
    void testMapEntryWithCustomMapFileEnding() {
        String customMapEnding = "test.json";
        MapEntry customEntry = MapEntry.of(tempDir, customMapEnding);
        assertNotNull(customEntry);
        assertFalse(customEntry.hasStandardEnding());
        assertNull(customEntry.getMapFile());

        customEntry.createFile();
        assertNotNull(customEntry.getMapFile());
    }
}