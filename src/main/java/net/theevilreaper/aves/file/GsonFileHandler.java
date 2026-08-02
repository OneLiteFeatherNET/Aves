package net.theevilreaper.aves.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minestom.server.utils.validate.Check;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Implementation of the {@link FileHandler} using the {@link Gson} library for JSON serialization and deserialization.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.1.0
 * @deprecated Deprecated since version 1.9.0 and will be removed in a future release. Use {@link ModernGsonFileHandler} instead.
 */
@Deprecated(since = "1.9.0", forRemoval = true)
public final class GsonFileHandler implements FileHandler {

    private final Gson gson;

    /**
     * Creates a new instance of the file handler with a default {@link Gson} instance.
     */
    public GsonFileHandler() {
        this.gson = new Gson();
    }

    /**
     * Creates a new instance of the file handler.
     *
     * @param gson the {@link Gson} instance to deserialize or serialize data
     */
    public GsonFileHandler(Gson gson) {
        this.gson = gson;
    }

    /**
     * Saves a given object into a file at the specified path.
     * Automatically creates parent directories if they do not exist.
     *
     * @param path   path where the file is located
     * @param object object to save
     * @param <T>    generic type for the object value
     */
    @Override
    public <T> void save(Path path, T object) {
        boolean isNewFile = prepareSavePath(path);

        try (var outputStream = Files.newBufferedWriter(path, UTF_8)) {
            if (isNewFile) {
                LOGGER.info("Created new file: {}", path.getFileName());
            }
            gson.toJson(object, TypeToken.get(object.getClass()).getType(), outputStream);
        } catch (IOException exception) {
            LOGGER.warn("Unable to save file", exception);
        }
    }

    /**
     * Loads a given file and deserializes its JSON content to the target class.
     *
     * @param path  path where the file is located
     * @param clazz generic class object
     * @param <T>   generic type for the object value
     * @return an {@link Optional} containing the deserialized object, or empty if the file does not exist
     */
    @Override
    public <T> Optional<T> load(Path path, Class<T> clazz) {
        Check.argCondition(Files.isDirectory(path), "Unable to load a directory. Please check the used path");
        if (!Files.exists(path)) {
            return Optional.empty();
        }

        try (var reader = Files.newBufferedReader(path, UTF_8)) {
            return Optional.ofNullable(gson.fromJson(reader, clazz));
        } catch (IOException exception) {
            LOGGER.warn("Unable to load file", exception);
        }
        return Optional.empty();
    }
}
