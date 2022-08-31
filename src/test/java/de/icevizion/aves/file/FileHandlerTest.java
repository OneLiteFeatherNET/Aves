package de.icevizion.aves.file;

import de.icevizion.aves.map.BaseMap;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileHandlerTest {

    private final String testMap = "test.json";

    private final FileHandler fileHandler = new GsonFileHandler();

    @TempDir
    static File tempDir;

    @Order(1)
    @Test
    void testCharset() {
        assertSame(StandardCharsets.UTF_8, FileHandler.UTF_8);
    }

    @Order(2)
    @Test
    void testGsonFileHandlerWrite() {
        var path = tempDir.toPath().resolve(testMap);
        var baseMap = BaseMap.of("TestMap");
        baseMap.setBuilders("Builder1", "Builder2");
        fileHandler.save(path, baseMap);
        assertTrue(Files.exists(path));
    }

    @Order(3)
    @Test
    void testGsonFileHandler() {
        var path = tempDir.toPath().resolve(testMap);
        var optional = fileHandler.load(path, BaseMap.class);

        assertTrue(optional.isPresent());

        var map = optional.get();

        assertEquals("TestMap", map.getName());
        assertArrayEquals(new String[]{"Builder1", "Builder2"}, map.getBuilders());
    }

    @Order(4)
    @Test
    void testFileNotExistsRead() {
        var file = fileHandler.load(tempDir.toPath().resolve("test3.json"), BaseMap.class);
        assertTrue(file::isEmpty);
    }
}