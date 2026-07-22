package net.theevilreaper.aves.file;

import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Interface representing the base logic to load or save JSON files.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 1.9.0
 */
public interface ModernFileHandler {

    Logger LOGGER = LoggerFactory.getLogger(ModernFileHandler.class);

    Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * Prepares a target file path for saving by validating it is not a directory and ensuring parent directories exist.
     *
     * @param path target path
     * @return true if the file does not exist yet, false otherwise
     */
    default boolean prepareSavePath(Path path) {
        return FileHandlerUtil.prepareSavePath(path, LOGGER);
    }

    /**
     * Saves a given object into a file at the specified path.
     *
     * @param path      path where the file is located
     * @param object    object to save
     * @param typeToken type token to serialize the object
     * @param <T>       generic type for the object value
     */
    <T> void save(Path path, T object, TypeToken<T> typeToken);

    /**
     * Loads a given file and deserializes its content to the target type.
     *
     * @param path      path where the file is located
     * @param typeToken type token to deserialize the object
     * @param <T>       generic type for the object value
     * @return an {@link Optional} containing the deserialized object, or empty if the file does not exist
     */
    <T> Optional<T> load(Path path, TypeToken<T> typeToken);
}