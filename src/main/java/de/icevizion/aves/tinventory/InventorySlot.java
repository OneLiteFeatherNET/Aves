package de.icevizion.aves.tinventory;

import de.icevizion.aves.item.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class InventorySlot extends Slot {

    private ItemStack itemStack;

    public InventorySlot(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public InventorySlot(ItemStack itemStack, Consumer<InventoryClickEvent> clickListener) {
        this.itemStack = itemStack;
        this.clickListener = clickListener;
    }
    public InventorySlot(ItemBuilder itemBuilder) {
        this.itemStack = itemBuilder.build();
    }

    public InventorySlot(ItemBuilder itemBuilder, Consumer<InventoryClickEvent> clickListener) {
        this.itemStack = itemBuilder.build();
        this.clickListener = clickListener;
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }
}
