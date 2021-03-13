package de.icevizion.aves.tinventory;

import de.icevizion.aves.item.ItemBuilder;
import de.icevizion.aves.tinventory.translated.TranslatedSlot;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class InventoryLayout implements Cloneable {

    private static final InventorySlot EMPTY_SLOT = new InventorySlot(new ItemStack(Material.AIR));

    private final ISlot[] contents;

    public InventoryLayout(int size) {
        contents = new ISlot[size];
    }

    public void applyLayout(ItemStack[] invContents, Locale locale) {
        for (int i = 0; i < invContents.length; i++) {
            var slot = getContents()[i];
            if (slot == null)
                continue;

            if (slot instanceof TranslatedSlot && locale == null) {
                throw new IllegalArgumentException("Tried to apply the InventoryLayout with an Translated slot and provided no locale!");
            }

            if (slot == EMPTY_SLOT) {
                invContents[i] = null;
            } else {
                invContents[i] = (slot instanceof TranslatedSlot) ? ((TranslatedSlot) slot).getItem(locale) : slot.getItem();
            }
        }
    }

    public InventoryLayout item(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> clickEvent) {
        contents[slot] = new InventorySlot(itemStack, clickEvent);

        return this;
    }

    public InventoryLayout item(int slot, ItemBuilder itemBuilder, Consumer<InventoryClickEvent> clickEvent) {
        contents[slot] = new InventorySlot(itemBuilder, clickEvent);

        return this;
    }

    public InventoryLayout item(int slot, ItemStack itemStack) {
        return item(slot, itemStack, null);
    }

    public InventoryLayout item(int slot, ItemBuilder itemBuilder) {
        return item(slot, itemBuilder, null);
    }

    public InventoryLayout item (int slot, ISlot slotItem) {
        contents[slot] = slotItem;

        return this;
    }

    public InventoryLayout block(int slot) {
        contents[slot] = EMPTY_SLOT;

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
