package de.icevizion.aves.item;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * The class represents a wrapper for an item which can hold one {@link ItemStack} per instance from the record class.
 * The record class represents a non-translatable item for the i18n functionality in the api.
 *
 * @author theEvilReaper
 * @since 1.0.12
 * @version 1.0.2
 */
public non-sealed class Item implements IItem {

    private final ItemStack itemStack;

    /**
     * Returns a new object from {@link Item}.
     *
     * @param itemStack The {@link ItemStack} for the {@link Item}
     */
    protected Item(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Returns a new object from {@link Item}.
     *
     * @param itemBuilder A valid instance from a {@link ItemStack.Builder}
     * @return The created instance from {@link Item}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Item of(@NotNull ItemStack.Builder itemBuilder) {
        return new Item(itemBuilder.build());
    }

    /**
     * Creates a new object reference from the {@link Item} with an given {@link ItemStack}.
     * @param stack the stack for the object
     * @return the created reference instance
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Item of(@NotNull ItemStack stack) {
        return new Item(stack);
    }

    /**
     * Returns a new object from {@link Item} with the given {@link Material}
     *
     * @param material The material for the {@link ItemStack}
     * @return The created instance from {@link Item}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Item of(@NotNull Material material) {
        return new Item(ItemStack.of(material));
    }

    /**
     * Returns the {@link ItemStack}.
     * In this implementation the language will be ignored
     *
     * @param ignore The locale from the player
     * @return The fetched stack
     */
    @Override
    public ItemStack get(@NotNull Locale ignore) {
        return itemStack;
    }
}
