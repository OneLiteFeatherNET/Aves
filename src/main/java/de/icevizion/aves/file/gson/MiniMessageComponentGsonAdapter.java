package de.icevizion.aves.file.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * This class is used to serialize and deserialize a {@link Component} object into mini message string representation.
 * The {@link Component} object is represented as a JSON object with the key "minimessage".
 * The JSON object is represented as follows:
 * <pre>
 *     {
 *     "minimessage": "miniMessage serialized component"
 *     }
 * </pre>
 * A component can be created by calling the {@link MiniMessage#miniMessage()} method.
 * Use {@link #create()} to create a default adapter.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @see Component
 * @see MiniMessage
 * @see MiniMessage#miniMessage()
 * @author TheMeinerLP
 */
public class MiniMessageComponentGsonAdapter extends TypeAdapter<Component> {

    /**
     * Creates a new instance of the {@link MiniMessageComponentGsonAdapter}.
     * @return the new instance of the {@link MiniMessageComponentGsonAdapter}
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull MiniMessageComponentGsonAdapter create() {
        return new MiniMessageComponentGsonAdapter();
    }

    @Override
    public void write(JsonWriter out, Component value) throws IOException {
        if (value == null) {
            return;
        }
        out.beginObject();
        out.name("minimessage").value(MiniMessage.miniMessage().serialize(value));
        out.endObject();
    }

    @Override
    public Component read(JsonReader in) throws IOException {
        in.beginObject();
        if (!in.nextName().equals("minimessage")) {
            throw new IOException("Expected component");
        }
        var component = MiniMessage.miniMessage().deserialize(in.nextString());
        in.endObject();
        return component;
    }
}
