package de.icevizion.aves.file;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Optional;

public final class JsonFileLoader {

    public static <T> void save(final File file, final T object, final Gson gson) {
        try (FileWriter fw = new FileWriter(file)) {
            if (!file.exists())
                if (file.createNewFile())
                   System.out.println("Created new file: " + file.getName());

            String json = gson.toJson(object);
            fw.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> Optional<T> load(final File file, final Class<T> clazz, final Gson gson) {
        if (!file.exists())
            return Optional.empty();

        try (FileReader fr = new FileReader(file)) {
            return Optional.of(gson.fromJson(fr, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static <T> Optional<T> load(final File file, final Type type, final Gson gson) {
        if (!file.exists())
            return Optional.empty();

        try (FileReader fr = new FileReader(file)) {
            return Optional.of(gson.fromJson(fr, type));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}