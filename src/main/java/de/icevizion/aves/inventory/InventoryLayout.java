package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.function.ApplyLayoutFunction;
import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.inventory.slot.ISlot;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import static de.icevizion.aves.inventory.util.InventoryConstants.CANCEL_CLICK;

/**
 * The {@link InventoryLayout} contains a bunch of method to set or update the layout of an inventory.
 *
 * @author theEvilReaper
 * @version 1.2.0
 * @since 1.0.0
 */
public sealed interface InventoryLayout permits InventoryLayoutImpl {

    /**
     * Creates a new {@link InventoryLayout} reference with a given {@link InventoryType}.
     *
     * @param type the type for the layout to get the size from it
     * @return the created reference
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull InventoryLayout fromType(@NotNull InventoryType type) {
        return new InventoryLayoutImpl(type);
    }

    /**
     * Creates a copy of a {@link InventoryLayout} reference.
     *
     * @param layout the layout which should be copied
     * @return the copied reference
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull InventoryLayout of(@NotNull InventoryLayout layout) {
        return new InventoryLayoutImpl((InventoryLayoutImpl) layout);
    }

    /**
     * Set a new reference from an implementation of the {@link ApplyLayoutFunction} functional interface.
     *
     * @param applyLayoutFunction the new implementation to set
     */
    void setApplyLayoutFunction(@NotNull ApplyLayoutFunction applyLayoutFunction);

    /**
     * Applies an array of  {@link ItemStack} to the layout.
     *
     * @param itemStacks the array which should be applied
     */
    void applyLayout(ItemStack[] itemStacks);

    /**
     * Applies an array of {@link ItemStack} to the layout with a given locale.
     *
     * @param itemStacks      the array which should be applied
     * @param locale          the locale to apply
     */
    void applyLayout(ItemStack[] itemStacks, Locale locale);

    /**
     * Set's a single item to one slot with a given listener.
     *
     * @param slot        the slot where the {@link ItemStack} should be set.
     * @param itemBuilder the builder reference which contains the information about the {@link ItemStack}
     * @param clickEvent  the event logic for the slot (can be null)
     * @return the layout reference
     */
    @NotNull
    default InventoryLayout setItem(int slot, @NotNull ItemStack.Builder itemBuilder, @Nullable InventoryClick clickEvent) {
        this.setItem(slot, itemBuilder.build(), clickEvent);
        return this;
    }

    /**
     * Set's a single item to one slot with a given listener.
     *
     * @param slot       the slot where the {@link ItemStack} should be set.
     * @param itemStack  the {@link ItemStack} for the slot
     * @param clickEvent the event logic for the slot (can be null)
     * @return the layout reference
     */
    @NotNull InventoryLayout setItem(int slot, @NotNull ItemStack itemStack, @Nullable InventoryClick clickEvent);

    /**
     * Set's a single item to one slot with a given listener.
     *
     * @param slot       the slot where the {@link ItemStack} should be set.
     * @param iSlot      a {@link ISlot} reference which contains data about the slot
     * @param clickEvent the event logic for the slot (can be null)
     * @return the layout reference
     */
    @NotNull InventoryLayout setItem(int slot, ISlot iSlot, InventoryClick clickEvent);

    /**
     * Set's a single item to a slot without a listener.
     *
     * @param slot      the slot where the {@link ItemStack} should be set.
     * @param itemStack the {@link ItemStack} for the slot
     * @return the layout reference
     */
    default @NotNull InventoryLayout setItem(int slot, ItemStack itemStack) {
        return this.setItem(slot, itemStack, null);
    }

    /**
     * Set's a single item to a slot without a listener.
     *
     * @param slot        the slot where the {@link ItemStack} should be set.
     * @param itemBuilder the builder reference which contains the information about the {@link ItemStack}
     * @return the layout reference
     */
    default @NotNull InventoryLayout setItem(int slot, @NotNull ItemStack.Builder itemBuilder) {
        return this.setItem(slot, itemBuilder.build());
    }

    /**
     * Set a {@link ISlot} reference to a specific slot index.
     *
     * @param slot     the slot index
     * @param slotItem the slot reference
     * @return the instance from the layout
     */
    @NotNull InventoryLayout setItem(int slot, @NotNull ISlot slotItem);

    /**
     * Set's a single {@link ItemStack} to each slot which is provided by the array.
     *
     * @param array the array which contains all indices where the item should be set
     * @param stack the {@link ItemStack} which should be set into each slot index
     * @return the instance from the layout
     */
    @NotNull
    default InventoryLayout setItems(int[] array, @NotNull ItemStack stack) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], stack);
        }
        return this;
    }

    /**
     * Set's a single {@link ItemStack} to each slot which is provided by the array.
     * Also, the method set's the given listener to each slot.
     *
     * @param array      the array which contains all indices where the item should be set
     * @param stack      the {@link ItemStack} which should be set into each slot index
     * @param clickEvent the listener for the slot
     * @return the instance from the layout
     */
    default @NotNull InventoryLayout setItems(int[] array, @NotNull ItemStack stack, @Nullable InventoryClick clickEvent) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], stack, clickEvent);
        }
        return this;
    }

    /**
     * Set's a single {@link ItemStack} to each slot which is provided by the array.
     *
     * @param array       the array which contains all indices where the item should be set
     * @param itemBuilder the builder reference which contains the information about the {@link ItemStack}
     * @param clickEvent  the listener for the slot
     * @return the instance from the layout
     */
    default @NotNull InventoryLayout setItems(int[] array, @NotNull ItemStack.Builder itemBuilder, @Nullable InventoryClick clickEvent) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], itemBuilder, clickEvent);
        }
        return this;
    }

    default @NotNull InventoryLayout setItems(int[] array, ItemStack.Builder itemBuilder) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], itemBuilder);
        }
        return this;
    }

    /**
     * Set's a single {@link ISlot} to each slot which is provided by the array.
     *
     * @param array the array which contains all indices where the item should be set
     * @param slot  the {@link ISlot} which should be set into each slot index
     * @return the instance from the layout
     */
    default @NotNull InventoryLayout setItems(int[] array, @NotNull ISlot slot) {
        for (int i = 0; i < array.length; i++) {
            setItem(array[i], slot);
        }
        return this;
    }

    /**
     * Blanks a single slot in the layout.
     *
     * @param slot the slot to blank
     */
    @NotNull InventoryLayout blank(int slot);

    /**
     * Marks an all given slot with a dummy slot object.
     *
     * @param slots the slots to mark
     */
    @NotNull
    default InventoryLayout blank(int... slots) {
        for (int i = 0; i < slots.length; i++) {
            this.blank(slots[i]);
        }
        return this;
    }

    /**
     * Removes the slot object at a given index.
     *
     * @param slot The index to remove the slot
     */
    @NotNull InventoryLayout clear(int slot);

    /**
     * Removes all slot object which stands in the given array.
     *
     * @param slots The array which contains all slot to remove
     */
    @NotNull
    default InventoryLayout clear(int... slots) {
        for (int i = 0; i < slots.length; i++) {
            this.clear(slots[i]);
        }
        return this;
    }

    /**
     * Updates the given listener from a slot.
     * If the listener is null the {@link de.icevizion.aves.inventory.util.InventoryConstants#CANCEL_CLICK} will set to the slot
     *
     * @param index    the index to get the slot to update
     * @param listener the listener to set
     */
    @NotNull InventoryLayout update(int index, @Nullable InventoryClick listener);

    /**
     * Updates the given ItemStack from a slot.
     * If the listener is null the {@link de.icevizion.aves.inventory.util.InventoryConstants#CANCEL_CLICK} will set to the slot
     *
     * @param index the index to get the slot to update
     * @param stack the {@link ItemStack} to set
     */
    @NotNull InventoryLayout update(int index, @Nullable ItemStack stack);

    /**
     * Updates an {@link ISlot} at a given index with a new {@link ItemStack}.
     *
     * @param index the slot index
     * @param stack the new stack tot set
     * @param click the click listener for the slot
     * @return the instance from the layout
     */
    @NotNull InventoryLayout update(int index, @NotNull ItemStack stack, @Nullable InventoryClick click);

    /**
     * Returns a slot from the content array by a specific index.
     *
     * @param index the index to get the slot
     * @return the fetched slot otherwise null
     */
    @Nullable
    default ISlot getSlot(int index) {
        Check.argCondition(index < 0 || index > getContents().length,
                "The given index does not fit into the array (0, " + getContents().length + ")");
        return getContents()[index];
    }

    /**
     * Removes a slot from the layout by a specific index.
     * @param index the index to remove the slot
     */
    @NotNull InventoryLayout remove(int index);

    /**
     * Removes a list of slots from the layout.
     * @param indices the array which contains the indices to remove
     */
    default @NotNull InventoryLayout remove(int @NotNull... indices) {
        for (int i = 0; i < indices.length; i++) {
            remove(indices[i]);
        }
        return this;
    }

    /**
     * Returns the array which contains all valid {@link ISlot}.
     *
     * @return the underlying array
     */
    @NotNull ISlot [] getContents();

    /**
     * Returns the size from the layout.
     *
     * @return the given size
     */
    default int getSize() {
        return this.getContents().length;
    }

    /**
     * Returns the given {@link ApplyLayoutFunction} instance.
     *
     * @return the underlying instance from the interface
     */
    @NotNull ApplyLayoutFunction getApplyLayoutFunction();
}
