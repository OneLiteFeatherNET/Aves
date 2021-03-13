package de.icevizion.aves.tinventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class InventoryLayout {

    private final ISlot[] contents;

    public InventoryLayout(int size) {
        contents = new ISlot[size];
    }

    public InventoryLayout item(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> clickEvent) {
        contents[slot] = new InventorySlot(itemStack, clickEvent);

        return this;
    }

    public InventoryLayout item(int slot, ItemStack itemStack) {
        return item(slot, itemStack, null);
    }

    public InventoryLayout item (int slot, ISlot slotItem) {
        contents[slot] = slotItem;

        return this;
    }

    //Todo: Utility functions like repeat() to repeat a given item for a vertical or horizontal row and stuff

    public InventoryLayout clear(int slot) {
        contents[slot] = null;

        return this;
    }

    public ISlot[] getContents() {
        return contents;
    }


    @Override
    public String toString() {
        return "InventoryLayout{" +
                "contents=" + Arrays.toString(contents) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryLayout)) return false;
        InventoryLayout that = (InventoryLayout) o;
        return Arrays.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(contents);
    }
}
