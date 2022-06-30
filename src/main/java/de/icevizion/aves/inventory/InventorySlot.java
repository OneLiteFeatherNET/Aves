package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.inventory.slot.Slot;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents a slot in a inventory which holds an {@link ItemStack}
 * and a {@link Consumer} with an {@link InventoryClick} to handle the click process on a slot.
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0.0
 * @since 1.0.0
 */
public class InventorySlot extends Slot {

    private ItemStack itemStack;

    /**
     * Creates a new instance from the {@link InventorySlot}.
     * @param itemStack The {@link ItemStack} to set
     */
    public InventorySlot(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Creates a new instance from the {@link InventorySlot}.
     * @param itemStack The {@link ItemStack} to set
     * @param inventoryClick The {@link InventoryClick} to set
     */
    public InventorySlot(@NotNull ItemStack itemStack, InventoryClick inventoryClick) {
        this.itemStack = itemStack;
        this.slot = inventoryClick;
    }

    /**
     * Creates a new instance from the {@link InventorySlot}.
     * @param itemBuilder The {@link ItemStack.Builder} were the item will be built from
     */
    public InventorySlot(@NotNull ItemStack.Builder itemBuilder) {
        this.itemStack = itemBuilder.build();
    }

    /**
     * Creates a new instance from the {@link InventorySlot}.
     * @param itemBuilder The {@link ItemStack.Builder} were the item will be built from
     * @param click The {@link InventoryClick} to set.
     */
    public InventorySlot(@NotNull ItemStack.Builder itemBuilder, InventoryClick click) {
        this.itemStack = itemBuilder.build();
        this.slot = click;
    }

    /**
     * Set the {@link ItemStack} for the {@link InventorySlot}.
     * @param itemStack The stack to set
     */
    public InventorySlot setItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    /**
     * Return the current {@link ItemStack}.
     * @return the {@link ItemStack}
     */
    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventorySlot that)) return false;
        return itemStack.equals(that.itemStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemStack);
    }

    /**
     * Returns a text representation of the {@link InventorySlot}.
     * @return a string which contains some attributes from the slot
     */
    @Override
    public String toString() {
        return "InventorySlot{" + "itemStack=" + itemStack.toString() + "} " + super.toString();
    }
}
