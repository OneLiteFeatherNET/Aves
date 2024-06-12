package de.icevizion.aves.i18n;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.translation.TranslationRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A translation registry that uses MiniMessage to translate components.
 * Without the usage of the class the translation with MiniMessage would not be possible.
 * @see TranslationRegistry
 */
public final class AvesTranslationRegistry implements TranslationRegistry {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final TranslationRegistry backedRegistry;

    /**
     * Creates a new instance of the translation registry.
     * @param backedRegistry the registry which should be used for the translation
     */
    public AvesTranslationRegistry(@NotNull TranslationRegistry backedRegistry) {
        this.backedRegistry = backedRegistry;
    }

    /**
     * Checks if the registry contains the given key.
     * @param key the key to check
     * @return true if the key is present in the registry
     */
    @Override
    public boolean contains(@NotNull String key) {
        return backedRegistry.contains(key);
    }

    /**
     * Returns the name of the registry as {@link Key}.
     * @return the given name from the registry
     */
    @Override
    public @NotNull Key name() {
        return backedRegistry.name();
    }

    /**
     * The method doesn't do anything in this implementation.
     * @param key a translation key
     * @param locale a locale
     * @return null
     */
    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        return null;
    }

    /**
     * Translates the given component with the provided locale.
     * @param component the component to translate
     * @param locale the locale to use
     * @return the translated component
     */
    @Override
    public @Nullable Component translate(@NotNull final TranslatableComponent component, @NotNull final Locale locale) {
        final MessageFormat translationFormat = backedRegistry.translate(component.key(), locale);

        if (translationFormat == null) return null;

        final String miniMessageString = translationFormat.toPattern();
        final Component resultingComponent;

        if (component.arguments().isEmpty()) {
            resultingComponent = MM.deserialize(miniMessageString);
        } else {
            resultingComponent = MM.deserialize(miniMessageString, new ArgumentTag(component.arguments()));
        }

        if (component.children().isEmpty()) {
            return resultingComponent;
        } else {
            return resultingComponent.children(component.children());
        }
    }

    /**
     * Sets the default locale for the registry.
     * @param locale the locale to use a default
     */
    @Override
    public void defaultLocale(@NotNull Locale locale) {
        backedRegistry.defaultLocale(locale);
    }

    /**
     * Registers a new translation in the registry.
     * @param key the key to register
     * @param locale the locale to register
     * @param format the format to register
     */
    @Override
    public void register(@NotNull String key, @NotNull Locale locale, @NotNull MessageFormat format) {
        backedRegistry.register(key, locale, format);
    }

    /**
     * Unregisters a translation from the registry.
     * @param key the key to unregister
     */
    @Override
    public void unregister(@NotNull String key) {
        backedRegistry.unregister(key);
    }

    private record ArgumentTag(List<? extends ComponentLike> argumentComponents) implements TagResolver {
        private static final String NAME = "argument";
        private static final String NAME_1 = "arg";

        private ArgumentTag(final @NotNull List<? extends ComponentLike> argumentComponents) {
            this.argumentComponents = Objects.requireNonNull(argumentComponents, "argumentComponents");
        }

        @Override
        public @NotNull Tag resolve(
                final @NotNull String name,
                final @NotNull ArgumentQueue arguments,
                final @NotNull Context ctx
        ) throws ParsingException {
            final int index = arguments.popOr("No argument number provided")
                    .asInt().orElseThrow(() -> ctx.newException("Invalid argument number", arguments));

            if (index < 0 || index >= argumentComponents.size()) {
                throw ctx.newException("Invalid argument number", arguments);
            }

            return Tag.inserting(argumentComponents.get(index));
        }

        @Override
        public boolean has(final @NotNull String name) {
            return name.equals(NAME) || name.equals(NAME_1);
        }
    }
}
