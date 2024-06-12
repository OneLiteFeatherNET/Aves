package de.icevizion.aves.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * The TextData is a record which holds some information about a message with optional arguments.
 * @param key the key for the message
 * @param args optional arguments for the message
 */
public record TextData(@NotNull String key, @NotNull Component... args) {

    /**
     * Creates a new instance of the TextData.
     * @param key the key for the message
     */
    public TextData(@NotNull String key) {
        this(key, new Component[0]);
    }

    /**
     * Creates a new instance of the TextData.
     * @param key the key for the message
     * @param args the arguments for the message
     */
    public TextData(@NotNull String key, @NotNull String... args) {
        this(key, Arrays.stream(args).filter(s -> !s.trim().isEmpty()).map(Component::text).toArray(Component[]::new));
    }

    /**
     * Returns a {@link TranslatableComponent} from the given data.
     * @return the created component
     */
    public @NotNull TranslatableComponent createComponent() {
        return args == null ? Component.translatable(key) : Component.translatable(key, args);
    }
}
