package net.theevilreaper.aves.file.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeyGsonAdapterTest {

    private static final String KEY_JSON = """
             {"namespace":"aves","value":"example"}""";
    private Key writeKey;
    private Gson gson;

    @BeforeAll
    void init() {
        this.writeKey = Key.key("aves:example");
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Key.class, KeyGsonAdapter.create())
                .registerTypeAdapter(Key.class, KeyGsonAdapter.createMinestom())
                .create();
    }

    @Test
    void testKeyWrite() {
        var output = this.gson.toJson(this.writeKey, Key.class);
        assertNotNull(output);
        assertEquals(KEY_JSON, output);
    }

    @Test
    void testKeyRead() {
        var fromJson = this.gson.fromJson(KEY_JSON, Key.class);
        assertNotNull(fromJson);
        assertEquals(this.writeKey, fromJson);
    }

    @Test
    void testNamespaceIDWrite() {
        var namespaceID = Key.key("aves:example");
        var output = this.gson.toJson(namespaceID, Key.class);
        assertNotNull(output);
        assertEquals(KEY_JSON, output);
    }

    @Test
    void testNamespaceIDRead() {
        var fromJson = this.gson.fromJson(KEY_JSON, Key.class);
        assertNotNull(fromJson);
        assertEquals(Key.key("aves:example"), fromJson);
    }

}