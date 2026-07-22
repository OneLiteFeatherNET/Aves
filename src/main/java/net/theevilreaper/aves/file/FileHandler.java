package net.theevilreaper.aves.file;

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
 * @version 1.0.0
 * @since 1.0.0
 * @deprecated Deprecated since version 1.9.0 and will be removed in a future release. Use {@link ModernFileHandler} instead.
 */
@Deprecated(since = "1.9.0", forRemoval = true)
public interface FileHandler {

    /**
     * The logger for the {@link FileHandler}.
     */
    Logger LOGGER = LoggerFactory.getLogger(FileHandler.class);

    /**
     * The default charset used for reading and writing files.
     */
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
     * Saves a given object into a file.
     *
     * @param path   The path where the file is located
     * @param object The object to save
     * @param <T>    A generic type for the object value
     */
    <T> void save(Path path, T object);

    /**
     * Load a given file and parse to the give class.
     *
     * @param path  is the where the file is located
     * @param clazz Represents the class which should be loaded
     * @param <T>   is generic type for the object value
     * @return a {@link Optional} with the object instance
     */
    <T> Optional<T> load(Path path, Class<T> clazz);
}