package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import de.icevizion.aves.inventory.function.ApplyLayoutFunction;
import de.icevizion.aves.inventory.function.DefaultApplyLayoutFunction;
import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.slot.TranslatedSlot;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Locale;

import static de.icevizion.aves.inventory.util.InventoryConstants.CANCEL_CLICK;
import static de.icevizion.aves.inventory.util.InventoryConstants.EMPTY_SLOT;

/**
 * Represents a layout which contains all items for an inventory.
 * @author Patrick Zdarsky / Rxcki
 * @since 1.0.12
 * @version 1.0.2
 */
@SuppressWarnings("java:S3776")
public class InventoryLayout {

    private static final String INDEX_ERROR = "The given slot index is out of range";
    private ApplyLayoutFunction applyLayoutFunction;
    private final ISlot[] contents;

    /**
     * Creates a new instance from the {@link InventoryLayout}.
     * @param type the given size for the layout
     */
    public InventoryLayout(@NotNull InventoryType type) {
        this.contents = new ISlot[type.getSize()];
        this.applyLayoutFunction = new DefaultApplyLayoutFunction(this.contents);
    }

    /**
     * Creates a deep copy from a given {@link InventoryLayout}.
     * @param layout the layout to copy
     */
    private InventoryLayout(@NotNull InventoryLayout layout) {
        this.contents = new ISlot[layout.getContents().length];
        for (int i = 0; i < layout.getContents().length; i++) {
            var slotEntry = layout.getContents()[i];
            if (slotEntry instanceof InventorySlot inventorySlot) {
                this.contents[i] = InventorySlot.of(inventorySlot);
            } else if (slotEntry instanceof TranslatedSlot translatedSlot) {
                this.contents[i] = TranslatedSlot.of(translatedSlot);
            } else {
                LoggerFactory.getLogger("Aves").info("Found a slot which can not be converted");
            }
        }
        this.applyLayoutFunction = layout.getApplyLayoutFunction();
    }

    /**
     * Creates a deep copy of a given {@link InventoryLayout}.
     * @param inventoryLayout the layout to copy
     * @return the created copy
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull InventoryLayout of(@NotNull InventoryLayout inventoryLayout) {
        return new InventoryLayout(inventoryLayout);
    }

    /**
     * Set a new reference from an implementation of the {@link ApplyLayoutFunction} functional interface.
     * @param applyLayoutFunction the new implementation to set
     */
    public void setApplyLayoutFunction(@NotNull ApplyLayoutFunction applyLayoutFunction) {
        this.applyLayoutFunction = applyLayoutFunction;
    }

    /**
     * Applies a given {@link ItemStack} array to the layout.
     * @param itemStacks the array which should be applied
     */
    public void applyLayout(ItemStack[] itemStacks) {
        this.applyLayoutFunction.applyLayout(itemStacks, null, null);
    }

    public void applyLayout(ItemStack[] itemStacks, Locale locale, MessageProvider messageProvider) {
        this.applyLayoutFunction.applyLayout(itemStacks, locale, messageProvider);
    }


    public InventoryLayout setItem(int slot, ItemStack.Builder itemBuilder, InventoryClick clickEvent) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        this.contents[slot] = new InventorySlot(itemBuilder, clickEvent);
        return this;
    }

    public InventoryLayout setItem(int slot, ItemStack itemStack, InventoryClick clickEvent) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        this.contents[slot] = new InventorySlot(itemStack, clickEvent);
        return this;
    }

    public InventoryLayout setItem(int slot, ISlot iSlot, InventoryClick clickEvent) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
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
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
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
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        this.contents[slot] = new InventorySlot(itemStack, CANCEL_CLICK);
        return this;
    }

    public InventoryLayout setNonClickItem(int slot, ISlot slotItem) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
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
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        this.contents[slot] = EMPTY_SLOT;
        return this;
    }

    /**
     * Marks an all given slot with a dummy slot object.
     * @param slots The slots to mark
     */
    public InventoryLayout blank(int... slots) {
        for (int i = 0; i < slots.length; i++) {
            this.blank(slots[i]);
        }
        return this;
    }

    /**
     * Removes the slot object at a given index.
     * @param slot The index to remove the slot
     */
    public InventoryLayout clear(int slot) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        contents[slot] = null;
        return this;
    }

    /**
     * Removes all slot object which stands in the given array.
     * @param slots The array which contains all slot to remove
     *
     */
    public InventoryLayout clear(int... slots) {
        for (int i = 0; i < slots.length; i++) {
            this.clear(slots[i]);
        }
        return this;
    }

    /**
     * Updates the given listener from a slot.
     * If the listener is null the {@link de.icevizion.aves.inventory.util.InventoryConstants#CANCEL_CLICK} will set to the slot
     * @param index The index to get the slot to update
     * @param listener The listener to set
     */
    public InventoryLayout update(int index, @Nullable InventoryClick listener) {
        Check.argCondition(index < 0 || index > contents.length, INDEX_ERROR);
        contents[index].setClick(listener == null ? CANCEL_CLICK : listener);
        return this;
    }

    /**
     * Updates the given ItemStack from a slot.
     * If the listener is null the {@link de.icevizion.aves.inventory.util.InventoryConstants#CANCEL_CLICK} will set to the slot
     * @param index The index to get the slot to update
     * @param stack The {@link ItemStack} to set
     */
    public InventoryLayout update(int index, @Nullable ItemStack stack) {
        Check.argCondition(index < 0 || index > contents.length, INDEX_ERROR);
        contents[index].setItemStack(stack);
        return this;
    }

    /**
     * Updates an {@link ISlot} at a given index with a new {@link ItemStack}.
     * @param index the slot index
     * @param stack the new stack tot set
     * @param click the click listener for the slot
     * @return the instance from the layout
     */
    public InventoryLayout update(int index, @NotNull ItemStack stack, @Nullable InventoryClick click) {
        Check.argCondition(index < 0 || index > contents.length, INDEX_ERROR);
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
        Check.argCondition(index < 0 || index > this.contents.length,
                "The given index does not fit into the array (0, " + this.contents.length + ")");
        return this.contents[index];
    }

    /**
     * Returns the array which contains all valid {@link ISlot}.
     * @return the underlying array
     */
    @NotNull
    public ISlot[] getContents() {
        return contents;
    }

    /**
     * Returns the size from the layout.
     * @return the given size
     */
    public int getSize() {
        return this.contents.length;
    }

    /**
     * Returns the given {@link ApplyLayoutFunction} instance.
     * @return the underlying instance from the interface
     */
    @NotNull
    public ApplyLayoutFunction getApplyLayoutFunction() {
        return applyLayoutFunction;
    }

    /**
     * Returns a textual representation from the layout class.
     * @return the textual representation
     */
    @Override
    public String toString() {
        return "InventoryLayout{" + "contents=" + Arrays.toString(contents) + '}';
    }

    /**
     * Checks if two layout are equals.
     * @param o the object to check
     * @return True when they are equal otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryLayout that)) return false;
        return Arrays.equals(contents, that.contents);
    }

    /**
     * Returns a hash value as int from the content which is in the layout array.
     * @return the determined value
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(contents);
    }
}
