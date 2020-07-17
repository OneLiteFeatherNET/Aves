package de.icevizion.aves.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class JsonFileLoader {

    /**
     * Saves a given object into a file.
     * @param path The path where the file is located
     * @param object The object to save
     * @param gson The gson instance
     * @param <T> A generic type for the object value
     */

    public static <T> void save(Path path, T object, Gson gson) {
        try (BufferedWriter fw = Files.newBufferedWriter(path)) {
            if (!Files.exists(path)) {
                System.out.println("Created new file: " + Files.createFile(path).getFileName().toString());
            }
            gson.toJson(object, TypeToken.get(object.getClass()).getType(), fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> Optional<T> load(Path path, Class<T> clazz, Gson gson) {
        if (!Files.exists(path))
            return Optional.empty();

        try (BufferedReader fr = Files.newBufferedReader(path)) {
            return Optional.of(gson.fromJson(fr, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static <T> Optional<T> load(Path path, Type type, Gson gson) {
        if (!Files.exists(path))
            return Optional.empty();

        try (BufferedReader fr = Files.newBufferedReader(path)) {
            return Optional.of(gson.fromJson(fr, type));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}