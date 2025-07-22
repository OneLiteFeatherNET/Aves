package net.theevilreaper.aves.file;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The class represents the base logic to load or save json files.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 * * @deprecated This interface is deprecated since version 1.9.0 and will be removed in a future release. Use {@link ModernFileHandler} instead.
 **/
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
     * Saves a given object into a file.
     *
     * @param path   The path where the file is located
     * @param object The object to save
     * @param <T>    A generic type for the object value
     */
    <T> void save(@NotNull Path path, @NotNull T object);

    /**
     * Load a given file and parse to the give class.
     *
     * @param path  is the where the file is located
     * @param clazz Represents the class which should be loaded
     * @param <T>   is generic type for the object value
     * @return a {@link Optional} with the object instance
     */
    <T> Optional<T> load(@NotNull Path path, @NotNull Class<T> clazz);
}