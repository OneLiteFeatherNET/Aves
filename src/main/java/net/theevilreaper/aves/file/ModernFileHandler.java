package net.theevilreaper.aves.file;

import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The class represents the base logic to load or save JSON files.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.9.0
 **/
public interface ModernFileHandler {

    Logger LOGGER = LoggerFactory.getLogger(ModernFileHandler.class);

    Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * Saves a given object into a file.
     *
     * @param path      The path where the file is located
     * @param object    The object to save
     * @param typeToken the type token to serialize the object
     * @param <T>       A generic type for the object value
     */
    <T> void save(Path path, T object, TypeToken<T> typeToken);

    /**
     * Load a given file and parse to the give class.
     *
     * @param path      is the where the file is located
     * @param typeToken the type token to deserialize the object
     * @param <T>       is generic type for the object value
     * @return a {@link Optional} with the object instance
     */
    <T> Optional<T> load(Path path, TypeToken<T> typeToken);
}