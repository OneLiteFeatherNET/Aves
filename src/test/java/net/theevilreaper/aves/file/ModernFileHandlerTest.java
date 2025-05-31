package net.theevilreaper.aves.file;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.theevilreaper.aves.map.BaseMap;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ModernFileHandlerTest {
    private final String testMap = "test.json";
    private final ModernFileHandler fileHandler = new ModernGsonFileHandler();

    @TempDir
    static File tempDir;

    @Order(1)
    @Test
    void testCharset() {
        assertSame(StandardCharsets.UTF_8, ModernFileHandler.UTF_8);
    }

    @Order(2)
    @Test
    void testOtherConstructor() {
        var fileLoader = new ModernGsonFileHandler(new Gson());
        assertNotNull(fileLoader);
    }
    @Order(3)
    @Test
    void testGsonFileHandlerWrite() {
        var path = tempDir.toPath().resolve(testMap);
        var baseMap = BaseMap.of("TestMap");
        baseMap.setBuilders("Builder1", "Builder2");
        fileHandler.save(path, baseMap, TypeToken.get(BaseMap.class));
        assertTrue(Files.exists(path));
    }

    @Order(4)
    @Test
    void testGsonFileHandler() {
        var path = tempDir.toPath().resolve(testMap);
        var optional = fileHandler.load(path, TypeToken.get(BaseMap.class));

        assertTrue(optional.isPresent());

        var map = optional.get();

        assertEquals("TestMap", map.getName());
        assertArrayEquals(new String[]{"Builder1", "Builder2"}, map.getBuilders());
    }

    @Order(5)
    @Test
    void testFileNotExistsRead() {
        var file = fileHandler.load(tempDir.toPath().resolve("test3.json"), TypeToken.get(BaseMap.class));
        assertTrue(file::isEmpty);
    }

    @Order(6)
    @Test
    void testInvalidPathSave() {
        var path = tempDir.toPath();
        var baseMap = BaseMap.of("TestMap");
        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> fileHandler.save(path, baseMap, TypeToken.get(BaseMap.class))
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
                () -> fileHandler.load(path, TypeToken.get(BaseMap.class))
        );
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertEquals("Unable to load a directory. Please check the used path", exception.getMessage());
    }
}
