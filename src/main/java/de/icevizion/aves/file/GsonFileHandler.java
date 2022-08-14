package de.icevizion.aves.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The class represents the implementation of the {@link FileHandler} for the {@link com.google.gson.Gson} library.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.1.0
 **/
public final class GsonFileHandler implements FileHandler {

    private final Gson gson;

    /**
     * Creates a new instance from the file handler.
     */
    public GsonFileHandler() {
        this.gson = new Gson();
    }

    /**
     * Creates a new instance from the file handler.
     * @param gson the gson instance to deserialize or serialize data
     */
    public GsonFileHandler(@NotNull Gson gson) {
        this.gson = gson;
    }

    /**
     * Saves a given object into a file.
     * @param path The path where the file is located
     * @param object The object to save
     * @param <T> A generic type for the object value
     */
    @Override
    public <T> void save(@NotNull Path path, @NotNull T object) {
        try (var outputStream = Files.newBufferedWriter(path, UTF_8)) {
            if (!Files.exists(path)) {
                LOGGER.info("Created new file: " + Files.createFile(path).getFileName().toString());
            }
            gson.toJson(object, TypeToken.get(object.getClass()).getType(), outputStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Load a given file and parse to the give class.
     * @param path is the where the file is located
     * @param clazz is the generic class object
     * @param <T> is generic type for the object value
     * @return a {@link Optional} with the object instance
     */
    @Override
    public <T> Optional<T> load(@NotNull Path path, @NotNull Class<T> clazz) {
        if (!Files.exists(path)) {
            return Optional.empty();
        }

        try (var reader = Files.newBufferedReader(path, UTF_8)) {
            return Optional.ofNullable(gson.fromJson(reader, clazz));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }
}
