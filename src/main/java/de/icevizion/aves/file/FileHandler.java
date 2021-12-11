package de.icevizion.aves.file;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The class represents the base logic to load or save json files.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface FileHandler {

    Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * Saves a given object into a file.
     * @param path The path where the file is located
     * @param object The object to save
     * @param <T> A generic type for the object value
     */

    <T> void save(@NotNull Path path, @NotNull T object);

    /**
     * Load a given file and parse to the give class.
     * @param path is the where the file is located
     * @param clazz Represents the class which should be loaded
     * @param <T> is generic type for the object value
     * @return a {@link Optional} with the object instance
     */

    <T> Optional<T> load(@NotNull Path path, @NotNull Class<T> clazz);
}
