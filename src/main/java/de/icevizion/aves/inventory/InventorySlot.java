package de.icevizion.aves.inventory;

import de.icevizion.aves.item.ItemBuilder;
import de.icevizion.aves.util.Items;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class InventorySlot extends Slot {

    private ItemStack itemStack;

    public InventorySlot(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public InventorySlot(@NotNull ItemStack itemStack, Consumer<InventoryClickEvent> clickListener) {
        this.itemStack = itemStack;
        this.clickListener = clickListener;
    }

    public InventorySlot(@NotNull ItemBuilder itemBuilder) {
        this.itemStack = itemBuilder.build();
    }

    public InventorySlot(@NotNull ItemBuilder itemBuilder, Consumer<InventoryClickEvent> clickListener) {
        this.itemStack = itemBuilder.build();
        this.clickListener = clickListener;
    }

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
        if (!(o instanceof InventorySlot)) return false;
        InventorySlot that = (InventorySlot) o;
        return itemStack.equals(that.itemStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemStack);
    }

    @Override
    public String toString() {
        return "InventorySlot{" +
                "itemStack=" + itemStack +
                "} " + super.toString();
    }
}
