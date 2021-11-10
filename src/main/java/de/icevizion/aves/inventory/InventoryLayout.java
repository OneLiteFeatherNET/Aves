package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.slot.TranslatedSlot;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.ItemStackBuilder;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */

public class InventoryLayout implements Cloneable {

    public static final Consumer<InventoryPreClickEvent> CANCEL_CONSUMER = clickEvent -> clickEvent.setCancelled(true);

    private static final InventorySlot EMPTY_SLOT = new InventorySlot(ItemStack.AIR);

    private ISlot[] contents;

    public InventoryLayout(InventoryRows rows) {
        this.contents = new ISlot[rows.getSize()];
    }

    public InventoryLayout(int size) {
        contents = new ISlot[size];
    }

    public void applyLayout(ItemStack[] itemStacks) {
        applyLayout(itemStacks, null, null);
    }

    public void applyLayout(ItemStack[] itemStacks, Locale locale, MessageProvider messageProvider) {
        for (int i = 0; i < itemStacks.length; i++) {
            ISlot slot = contents[i];

            if (slot == null) {
                continue;
            }

            if (slot instanceof TranslatedSlot && locale == null) {
                throw new IllegalArgumentException("Tried to apply the InventoryLayout with an Translated slot and provided no locale!");
            }
            if (slot == EMPTY_SLOT) {
                itemStacks[i] = null;
            } else {
                if (slot instanceof TranslatedSlot translatedSlot) {
                    if (translatedSlot.getTranslatedItem().getMessageProvider() == null) {
                        translatedSlot.getTranslatedItem().setMessageProvider(messageProvider);
                    }

                    itemStacks[i] = translatedSlot.getItem(locale);
                } else {
                    itemStacks[i] = slot.getItem();
                }
            }
        }
    }

    public InventoryLayout setItem(int slot, ItemStackBuilder itemBuilder, Consumer<InventoryPreClickEvent> clickEvent) {
        this.contents[slot] = new InventorySlot(itemBuilder, clickEvent);
        return this;
    }

    public InventoryLayout setItem(int slot, ItemStack itemStack, Consumer<InventoryPreClickEvent> clickEvent) {
        this.contents[slot] = new InventorySlot(itemStack, clickEvent);
        return this;
    }

    public InventoryLayout setItem(int slot, ISlot iSlot, Consumer<InventoryPreClickEvent> clickEvent) {
        iSlot.setClickListener(clickEvent);
        contents[slot] = iSlot;
        return this;
    }

    public InventoryLayout setItem(int slot, ItemStack itemStack) {
        return setItem(slot, itemStack, null);
    }

    public InventoryLayout setItem(int slot, ItemStackBuilder itemBuilder) {
        return setItem(slot, itemBuilder, null);
    }

    public InventoryLayout setItem(int slot, ISlot slotItem) {
        contents[slot] = slotItem;
        return this;
    }

    public InventoryLayout setItems(int[] array, ItemStackBuilder itemBuilder, Consumer<InventoryPreClickEvent> clickEvent) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], itemBuilder, clickEvent);
        }
        return this;
    }

    public InventoryLayout setItems(int[] array, ItemStack stack, Consumer<InventoryPreClickEvent> clickEvent) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], stack, clickEvent);
        }
        return this;
    }

    public InventoryLayout setItems(int[] array, ItemStackBuilder itemBuilder) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], itemBuilder);
        }
        return this;
    }

    public InventoryLayout setItems(int[] array, ItemStack stack) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], stack);
        }
        return this;
    }

    public InventoryLayout setItems(int[] array, ISlot slot) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i] , slot);
        }
        return this;
    }

    public InventoryLayout setNonClickItem(int slot, ItemStack itemStack) {
        this.contents[slot] = new InventorySlot(itemStack, CANCEL_CONSUMER);
        return this;
    }

    public InventoryLayout setNonClickItem(int slot, ISlot slotItem) {
        slotItem.setClickListener(CANCEL_CONSUMER);
        contents[slot] = slotItem;
        return this;
    }

    public InventoryLayout setNonClickItems(int[] array, ISlot slot) {
        for (int i = 0; i < array.length; i++) {
            setNonClickItem(array[i] , slot);
        }
        return this;
    }

    public InventoryLayout setNonClickItems(int[] array, ItemStackBuilder itemBuilder) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], itemBuilder, CANCEL_CONSUMER);
        }
        return this;
    }

    public InventoryLayout setNonClickItems(int[] array, ItemStack stack) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], stack, CANCEL_CONSUMER);
        }
        return this;
    }

    public InventoryLayout blank(int slot) {
        this.contents[slot] = EMPTY_SLOT;
        return this;
    }

    public InventoryLayout blank(int... slots) {
        for (int i = 0; i < slots.length; i++) {
            contents[slots[i]] = EMPTY_SLOT;
        }
        return this;
    }

    public InventoryLayout clear(int slot) {
        contents[slot] = null;
        return this;
    }

    /**
     * Returns the array which contains all valid {@link ISlot}.
     * @return the underlying array
     */

    public ISlot[] getContents() {
        return contents;
    }

    @Override
    public InventoryLayout clone() {
        try {
            InventoryLayout clone = (InventoryLayout) super.clone();
            clone.contents = contents.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("This should never happen", e);
        }
    }

    @Override
    public String toString() {
        return "InventoryLayout{" + "contents=" + Arrays.toString(contents) + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryLayout that)) return false;
        return Arrays.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(contents);
    }
}