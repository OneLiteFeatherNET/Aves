package net.theevilreaper.aves.map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapEntryTest {

    @TempDir
    static Path tempDir;

    @AfterEach
    void tearDown() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempDir)) {
            for (Path file : stream) {
                Files.delete(file);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

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
    void testMapEntryWithStandardFileEnding() {
        MapEntry standardEntry = MapEntry.of(tempDir);
        assertNotNull(standardEntry);
        assertEquals(tempDir, standardEntry.getDirectoryRoot());
        assertTrue(standardEntry.hasStandardEnding());
        assertNull(standardEntry.getMapFile());
        try {
            Files.createFile(tempDir.resolve(MapEntry.MAP_FILE));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        standardEntry.refresh();
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
        try {
            Files.createFile(tempDir.resolve(customMapEnding));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        customEntry.refresh();
        assertNotNull(customEntry.getMapFile());
    }
}