package de.icevizion.aves.file.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.key.Key;
import net.minestom.server.utils.NamespaceID;
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
                .registerTypeAdapter(NamespaceID.class, KeyGsonAdapter.createMinestom())
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
        var namespaceID = NamespaceID.from("aves:example");
        var output = this.gson.toJson(namespaceID, NamespaceID.class);
        assertNotNull(output);
        assertEquals(KEY_JSON, output);
    }

    @Test
    void testNamespaceIDRead() {
        var fromJson = this.gson.fromJson(KEY_JSON, NamespaceID.class);
        assertNotNull(fromJson);
        assertEquals(NamespaceID.from("aves:example"), fromJson);
    }

}