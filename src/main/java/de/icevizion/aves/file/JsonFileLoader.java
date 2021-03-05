package de.icevizion.aves.file;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.2
 */

public final class JsonFileLoader {

    /**
     * Saves a given object into a file.
     * @param path The path where the file is located
     * @param object The object to save
     * @param <T> A generic type for the object value
     */

    public static <T> void save(Path path, T object) {
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            if (!Files.exists(path)) {
                System.out.println("Created new file: " + Files.createFile(path).getFileName().toString());
            }
            JsonStream.serialize(object, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Load a given file and parse to the give class.
     * @param path is the where the file is located
     * @param clazz is the to parsed class
     * @param <T> is generic type for the object value
     * @return a optonial with the object instance
     */
    public static <T> Optional<T> load(Path path, Class<T> clazz) {
        if (!Files.exists(path))
            return Optional.empty();

        try (BufferedReader fr = Files.newBufferedReader(path)) {
            return Optional.of(JsonIterator.deserialize(fr.lines().collect(Collectors.joining()), clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}