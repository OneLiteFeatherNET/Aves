package de.icevizion.aves.item;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.ItemStackBuilder;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

/**
 * The {@link Item} represents an {@link ItemStack} which doesn't use the translation over i18n.
 * The class is a simple wrapper. It is used to allow structures where there are translated items and those that are not translated.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.13
 **/

public record Item(ItemStack itemStack) implements IItem {

    /**
     * Returns a new object from {@link Item}.
     *
     * @param itemStack The {@link ItemStack} for the {@link Item}
     */

    public Item(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Returns a new object from {@link Item}.
     *
     * @param itemBuilder A valid instance from a {@link ItemStackBuilder}
     * @return The created instance from {@link Item}
     */

    public static Item of(@NotNull ItemStackBuilder itemBuilder) {
        return new Item(itemBuilder.build());
    }

    /**
     * Returns a new object from {@link Item} with the given {@link Material}
     *
     * @param material The material for the {@link ItemStack}
     * @return The created instance from {@link Item}
     */

    public static Item of(@NotNull Material material) {
        return new Item(ItemStack.of(material));
    }

    /**
     * Returns the the {@link ItemStack}.
     * In this implementation the language will be ignored
     *
     * @param locale The locale from the player
     * @return The fetched stack
     */

    @Override
    public ItemStack get(@NotNull Locale locale) {
        return itemStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(itemStack, item.itemStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemStack);
    }
}
