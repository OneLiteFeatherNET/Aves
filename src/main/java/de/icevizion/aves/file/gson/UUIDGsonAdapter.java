package de.icevizion.aves.file.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

/**
 * This class is used to serialize and deserialize {@link UUID} objects.
 * It is used by the {@link com.google.gson.Gson} instance to convert the object to a JSON string.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @see TypeAdapter
 * @since 1.6.0
 */
public final class UUIDGsonAdapter extends TypeAdapter<UUID> {

    /**
     * Writes the given {@link UUID} object to the {@link JsonWriter}.
     *
     * @param jsonWriter the writer to write the object
     * @param uuid       the object to write
     * @throws IOException if an error occurs during the writing process
     */
    @Override
    public void write(@NotNull JsonWriter jsonWriter, @Nullable UUID uuid) throws IOException {
        if (uuid == null) return;
        jsonWriter.beginObject();
        jsonWriter.name("mostSigBits").value(uuid.getMostSignificantBits());
        jsonWriter.name("leastSigBits").value(uuid.getLeastSignificantBits());
        jsonWriter.endObject();
    }

    /**
     * Reads the {@link UUID} object from the {@link JsonReader}.
     *
     * @param jsonReader the reader to read the object
     * @return the deserialized object
     * @throws IOException if an error occurs during the reading process
     */
    @Override
    public @NotNull UUID read(@NotNull JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        if (!jsonReader.nextName().equals("mostSigBits")) {
            throw new IOException("Expected mostSigBits");
        }
        long mostSigBits = jsonReader.nextLong();
        if (!jsonReader.nextName().equals("leastSigBits")) {
            throw new IOException("Expected leastSigBits");
        }
        long leastSigBits = jsonReader.nextLong();
        jsonReader.endObject();
        return new UUID(mostSigBits, leastSigBits);
    }
}
