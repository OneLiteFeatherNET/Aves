package de.icevizion.aves.item;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

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
     * @param itemBuilder A valid instance from a {@link ItemStack.Builder}
     * @return The created instance from {@link Item}
     */
    public static Item of(@NotNull ItemStack.Builder itemBuilder) {
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
}
