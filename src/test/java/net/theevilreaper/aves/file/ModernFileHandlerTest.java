package net.theevilreaper.aves.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.theevilreaper.aves.map.BaseMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModernFileHandlerTest {

    private final ModernFileHandler fileHandler = new ModernGsonFileHandler();

    @Test
    void testCharset() {
        assertSame(StandardCharsets.UTF_8, ModernFileHandler.UTF_8);
    }

    @Test
    void testCustomGsonConstructor() {
        var fileLoader = new ModernGsonFileHandler(new Gson());
        assertNotNull(fileLoader);
    }

    @Test
    void testSaveAndLoadObject(@TempDir Path tempDir) {
        var path = tempDir.resolve("test.json");
        var baseMap = new BaseMap("TestMap", null, List.of("Builder1", "Builder2"));

        fileHandler.save(path, baseMap, TypeToken.get(BaseMap.class));
        assertTrue(Files.exists(path));

        var loadedMap = fileHandler.load(path, TypeToken.get(BaseMap.class));
        assertTrue(loadedMap.isPresent());
        assertEquals("TestMap", loadedMap.get().name());
        assertEquals(List.of("Builder1", "Builder2"), loadedMap.get().builders());
    }

    @Test
    void testSaveCreatesParentDirectories(@TempDir Path tempDir) {
        var nestedPath = tempDir.resolve("maps/lobby/map.json");
        var baseMap = new BaseMap("NestedMap", null, List.of("Builder1"));

        fileHandler.save(nestedPath, baseMap, TypeToken.get(BaseMap.class));
        assertTrue(Files.exists(nestedPath));

        var loadedMap = fileHandler.load(nestedPath, TypeToken.get(BaseMap.class));
        assertTrue(loadedMap.isPresent());
        assertEquals("NestedMap", loadedMap.get().name());
    }

    @Test
    void testGenericListSerialization(@TempDir Path tempDir) {
        var path = tempDir.resolve("items.json");
        List<String> items = List.of("item1", "item2", "item3");
        TypeToken<List<String>> token = new TypeToken<>() {};

        fileHandler.save(path, items, token);
        assertTrue(Files.exists(path));

        var loadedList = fileHandler.load(path, token);
        assertTrue(loadedList.isPresent());
        assertEquals(items, loadedList.get());
    }

    @Test
    void testLoadNonExistentFileReturnsEmpty(@TempDir Path tempDir) {
        var path = tempDir.resolve("non_existent.json");
        var result = fileHandler.load(path, TypeToken.get(BaseMap.class));
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveDirectoryThrowsException(@TempDir Path tempDir) {
        var baseMap = new BaseMap("TestMap", null, null);
        var typeToken = TypeToken.get(BaseMap.class);

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> fileHandler.save(tempDir, baseMap, typeToken)
        );
        assertEquals("Unable to save a directory. Please check the used path", exception.getMessage());
    }

    @Test
    void testLoadDirectoryThrowsException(@TempDir Path tempDir) {
        var typeToken = TypeToken.get(BaseMap.class);

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> fileHandler.load(tempDir, typeToken)
        );
        assertEquals("Unable to load a directory. Please check the used path", exception.getMessage());
    }
}
