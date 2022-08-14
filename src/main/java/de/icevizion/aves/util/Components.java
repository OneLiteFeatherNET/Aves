package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The class contains some usefully methods for {@link Component} from adventure.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public final class Components {

    private Components() {}

    /**
     * Strips the {@link TextDecoration#ITALIC} from a given component.
     * @param component the component
     * @return the component without the {@link TextDecoration#ITALIC}
     */
    public static @NotNull Component stripItalics(@NotNull Component component) {
        if (component.decoration(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET) {
            component = component.decoration(TextDecoration.ITALIC, false);
        }
        return component;
    }

    /**
     * Strips the {@link TextDecoration#ITALIC} from a given {@link ItemStack}.
     * @param itemStack the component
     * @return the {@link ItemStack} without the {@link TextDecoration#ITALIC}
     */
    @Contract("_ -> new")
    public static @NotNull ItemStack stripItalics(@NotNull ItemStack itemStack) {
        return itemStack.withDisplayName(Components::stripItalics)
                .withLore(lore -> lore.stream().map(Components::stripItalics).toList());
    }
}
