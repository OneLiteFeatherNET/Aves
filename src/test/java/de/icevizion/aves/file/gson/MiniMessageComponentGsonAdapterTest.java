package de.icevizion.aves.file.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MiniMessageComponentGsonAdapterTest {

    private static final String KEY_JSON = """
            {"minimessage":"\\u003cred\\u003eexample"}""";

    private Component writeKey;
    private Gson gson;

    @BeforeAll
    void init() {
        this.writeKey = MiniMessage.miniMessage().deserialize("<red>example</red>");
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, MiniMessageComponentGsonAdapter.create())
                .create();
    }

    @Test
    void testComponentWrite() {
        var output = this.gson.toJson(this.writeKey, Component.class);
        assertNotNull(output);
        assertEquals(KEY_JSON, output);
    }

    @Test
    void testComponentRead() {
        var fromJson = this.gson.fromJson(KEY_JSON, Component.class);
        assertNotNull(fromJson);
        assertEquals(this.writeKey, fromJson);
    }

}