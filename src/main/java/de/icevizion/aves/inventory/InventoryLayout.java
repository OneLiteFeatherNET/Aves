package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.slot.TranslatedSlot;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;

/**
 * Represents a layout which contains all items for an inventory.
 * @author Patrick Zdarsky / Rxcki
 * @since 1.0.12
 * @version 1.0.2
 */
public class InventoryLayout implements Cloneable {

    public static final InventoryClick CANCEL_CLICK = (player, clickType, slotID, condition) -> condition.setCancel(true);

    /**
     * An empty slot which is used as a marker / dummy slot to blank slots.
     */
    private static final InventorySlot EMPTY_SLOT = new InventorySlot(ItemStack.AIR);

    private ISlot[] contents;

    public InventoryLayout(@NotNull InventoryType type) {
        this.contents = new ISlot[type.getSize()];
    }

    public InventoryLayout(int size) {
        contents = new ISlot[size];
    }

    public void applyLayout(ItemStack[] itemStacks) {
        applyLayout(itemStacks, null, null);
    }

    public void applyLayout(ItemStack[] itemStacks, Locale locale, MessageProvider messageProvider) {
        if (itemStacks == null || itemStacks.length == 0) return;
        for (int i = 0; i < itemStacks.length; i++) {
            ISlot slot = contents[i];

            if (slot == null) continue;

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

    public InventoryLayout setItem(int slot, ItemStack.Builder itemBuilder, InventoryClick clickEvent) {
        this.contents[slot] = new InventorySlot(itemBuilder, clickEvent);
        return this;
    }

    public InventoryLayout setItem(int slot, ItemStack itemStack, InventoryClick clickEvent) {
        this.contents[slot] = new InventorySlot(itemStack, clickEvent);
        return this;
    }

    public InventoryLayout setItem(int slot, ISlot iSlot, InventoryClick clickEvent) {
        iSlot.setClick(clickEvent);
        contents[slot] = iSlot;
        return this;
    }

    public InventoryLayout setItem(int slot, ItemStack itemStack) {
        return setItem(slot, itemStack, null);
    }

    public InventoryLayout setItem(int slot, ItemStack.Builder itemBuilder) {
        return setItem(slot, itemBuilder, null);
    }

    public InventoryLayout setItem(int slot, ISlot slotItem) {
        contents[slot] = slotItem;
        return this;
    }

    public InventoryLayout setItems(int[] array, ItemStack.Builder itemBuilder, InventoryClick clickEvent) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], itemBuilder, clickEvent);
        }
        return this;
    }

    public InventoryLayout setItems(int[] array, ItemStack stack, InventoryClick clickEvent) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], stack, clickEvent);
        }
        return this;
    }

    public InventoryLayout setItems(int[] array, ItemStack.Builder itemBuilder) {
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
        this.contents[slot] = new InventorySlot(itemStack, CANCEL_CLICK);
        return this;
    }

    public InventoryLayout setNonClickItem(int slot, ISlot slotItem) {
        slotItem.setClick(CANCEL_CLICK);
        contents[slot] = slotItem;
        return this;
    }

    public InventoryLayout setNonClickItems(int[] array, ISlot slot) {
        for (int i = 0; i < array.length; i++) {
            setNonClickItem(array[i] , slot);
        }
        return this;
    }

    public InventoryLayout setNonClickItems(int[] array, ItemStack.Builder itemBuilder) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], itemBuilder, CANCEL_CLICK);
        }
        return this;
    }

    public InventoryLayout setNonClickItems(int[] array, ItemStack stack) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], stack, CANCEL_CLICK);
        }
        return this;
    }

    /**
     * Blanks a single slot in the layout.
     * @param slot The slot to blank
     */
    public InventoryLayout blank(int slot) {
        this.contents[slot] = EMPTY_SLOT;
        return this;
    }

    /**
     * Marks a all given slot with a dummy slot object.
     * @param slots The slots to mark
     */
    public InventoryLayout blank(int... slots) {
        for (int i = 0; i < slots.length; i++) {
            contents[slots[i]] = EMPTY_SLOT;
        }
        return this;
    }

    /**
     * Removes the slot object at a given index.
     * @param slot The index to remove the slot
     */
    public InventoryLayout clear(int slot) {
        contents[slot] = EMPTY_SLOT;
        return this;
    }

    /**
     * Removes all slot object which stands in the given array.
     * @param slots The array which contains all slot to remove
     *
     */
    public InventoryLayout clear(int... slots) {
        for (int i = 0; i < slots.length; i++) {
            contents[i] = EMPTY_SLOT;
        }
        return this;
    }

    /**
     * Updates the given listener from a slot.
     * If the listener is null the @{link {@link #CANCEL_CLICK} will set to the slot
     * @param index The index to get the slot to update
     * @param listener The listener to set
     */
    public InventoryLayout update(int index, @Nullable InventoryClick listener) {
        contents[index].setClick(listener == null ? CANCEL_CLICK : listener);
        return this;
    }

    public InventoryLayout update(int index, @NotNull ItemStack stack, @Nullable InventoryClick click) {
        var slot = contents[index];
        slot.setItemStack(stack);
        slot.setClick(click == null ? CANCEL_CLICK : click);
        return this;
    }

    /**
     * Returns a slot from the content array by a specific index.
     * @param index The index to get the slot
     * @return The fetched slot otherwise null
     */
    @Nullable
    public ISlot getSlot(int index) {
        if (index < 0 || index > this.contents.length) {
            throw new IllegalArgumentException("The given index " + index + "is not in the range of the array(0, "
                    + this.contents.length + ")");
        }

        return this.contents[index];
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