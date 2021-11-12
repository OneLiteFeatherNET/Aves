package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.slot.Slot;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.ItemStackBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
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
     * @param clickListener The {@link InventoryPreClickEvent} to set.
     */

    public InventorySlot(@NotNull ItemStack itemStack, Consumer<InventoryPreClickEvent> clickListener) {
        this.itemStack = itemStack;
        this.clickListener = clickListener;
    }

    /**
     * Creates a new instance from the {@link InventorySlot}.
     * @param itemBuilder The {@link ItemStackBuilder} were the item will be built from
     */

    public InventorySlot(@NotNull ItemStackBuilder itemBuilder) {
        this.itemStack = itemBuilder.build();
    }

    /**
     * Creates a new instance from the {@link InventorySlot}.
     * @param itemBuilder The {@link ItemStackBuilder} were the item will be built from
     * @param clickListener The {@link InventoryPreClickEvent} to set.
     */

    public InventorySlot(@NotNull ItemStackBuilder itemBuilder, Consumer<InventoryPreClickEvent> clickListener) {
        this.itemStack = itemBuilder.build();
        this.clickListener = clickListener;
    }

    /**
     * Return the current {@link ItemStack}.
     * @return the {@link ItemStack}
     */

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    public InventorySlot setItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
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

    @Override
    public String toString() {
        return "InventorySlot{" + "itemStack=" + itemStack.toString() + "} " + super.toString();
    }
}
