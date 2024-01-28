package de.icevizion.aves.file;

import com.google.gson.Gson;
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
    void testOtherConstructor() {
        var fileLoader = new GsonFileHandler(new Gson());
        assertNotNull(fileLoader);
    }
    @Order(3)
    @Test
    void testGsonFileHandlerWrite() {
        var path = tempDir.toPath().resolve(testMap);
        var baseMap = BaseMap.of("TestMap");
        baseMap.setBuilders("Builder1", "Builder2");
        fileHandler.save(path, baseMap);
        assertTrue(Files.exists(path));
    }

    @Order(4)
    @Test
    void testGsonFileHandler() {
        var path = tempDir.toPath().resolve(testMap);
        var optional = fileHandler.load(path, BaseMap.class);

        assertTrue(optional.isPresent());

        var map = optional.get();

        assertEquals("TestMap", map.getName());
        assertArrayEquals(new String[]{"Builder1", "Builder2"}, map.getBuilders());
    }

    @Order(5)
    @Test
    void testFileNotExistsRead() {
        var file = fileHandler.load(tempDir.toPath().resolve("test3.json"), BaseMap.class);
        assertTrue(file::isEmpty);
    }

    @Order(6)
    @Test
    void testInvalidPathSave() {
        var path = tempDir.toPath();
        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> fileHandler.save(path, BaseMap.class)
        );
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertEquals("Unable to save a directory. Please check the used path", exception.getMessage());
    }

    @Order(7)
    @Test
    void testInvalidPathLoad() {
        var path = tempDir.toPath();
        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> fileHandler.load(path, BaseMap.class)
        );
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertEquals("Unable to load a directory. Please check the used path", exception.getMessage());
    }
}