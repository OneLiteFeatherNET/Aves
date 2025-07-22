package net.theevilreaper.aves.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * The TextData is a record which holds some information about a message with optional arguments.
 *
 * @param key  the key for the message
 * @param args optional arguments for the message
 */
@SuppressWarnings("java:S6218")
public record TextData(@NotNull String key, @NotNull Component... args) {

    /**
     * Creates a new instance of the TextData.
     *
     * @param key the key for the message
     * @return the created instance
     */
    public static @NotNull TextData of(@NotNull String key, @NotNull Component... args) {
        return new TextData(key, args);
    }

    /**
     * Creates a new instance of the TextData.
     *
     * @param key  the key for the message
     * @param args the arguments for the message
     * @return the created instance with the given key and arguments
     */
    public static @NotNull TextData of(@NotNull String key, @NotNull String... args) {
        return new TextData(key, args);
    }

    /**
     * Creates a new instance of the TextData.
     *
     * @param key the key for the message
     */
    public TextData(@NotNull String key) {
        this(key, new Component[0]);
    }

    /**
     * Creates a new instance of the TextData.
     *
     * @param key  the key for the message
     * @param args the arguments for the message
     */
    public TextData(@NotNull String key, @NotNull String... args) {
        this(key, Arrays.stream(args).filter(s -> !s.trim().isEmpty()).map(Component::text).toArray(Component[]::new));
    }

    /**
     * Returns a {@link TranslatableComponent} from the given data.
     *
     * @return the created component
     */
    public @NotNull TranslatableComponent createComponent() {
        return args == null ? Component.translatable(key) : Component.translatable(key, args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextData textData = (TextData) o;
        return Objects.equals(key, textData.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }
}
