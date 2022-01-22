package de.icevizion.aves.file;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class represents the implementation of the {@link FileHandler} for the Jsoniter library.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.1.0
 **/
public class JsoniterFileHandler implements FileHandler {

    /**
     * Saves a given object into a file.
     * @param path The path where the file is located
     * @param object The object to save
     * @param <T> A generic type for the object value
     */
    public <T> void save(@NotNull Path path, @NotNull T object) {
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
     * @param clazz Represents the class which should be loaded
     * @param <T> is generic type for the object value
     * @return a {@link Optional} with the object instance
     */
    public <T> Optional<T> load(@NotNull Path path, @NotNull Class<T> clazz) {
        if (!Files.exists(path))
            return Optional.empty();

        try (BufferedReader fr = Files.newBufferedReader(path, UTF_8)) {
            return Optional.of(JsonIterator.deserialize(fr.lines().collect(Collectors.joining()), clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
