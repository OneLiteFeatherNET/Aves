package net.theevilreaper.aves.inventory.slot;

import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.theevilreaper.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link EmptySlot} is an implementation of the {@link ISlot} interface that represents an empty slot.
 * It does not allow setting a click or {@link ItemStack} and always returns an empty item stack.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.12
 */
public final class EmptySlot implements ISlot {

    /**
     * Sets a new reference from the {@link InventoryClick} to the slot.
     * Throws an {@link UnsupportedOperationException} since an empty slot cannot have a click.
     *
     * @param slot the slot to set
     * @return nothing due to the exception
     */
    @Override
    public @NotNull ISlot setClick(@NotNull InventoryClick slot) {
        throw new UnsupportedOperationException("Cannot set a click on an empty slot");
    }

    /**
     * Sets a new reference from an {@link ItemStack} to the slot.
     * Throws an {@link UnsupportedOperationException} since an empty slot cannot have an item stack.
     *
     * @param itemStack the stack to set
     * @return nothing due to the exception
     */
    @Override
    public @NotNull ISlot setItemStack(ItemStack itemStack) {
        throw new UnsupportedOperationException("Cannot set an item on an empty slot");
    }

    /**
     * Returns the click for the slot.
     *
     * @return a constant cancel click, indicating that no action can be performed
     */
    @Override
    public @NotNull InventoryClick getClick() {
        return InventoryConstants.CANCEL_CLICK;
    }

    /**
     * Returns the item stack for the slot.
     * Always returns {@link ItemStack#AIR} since this is an empty slot.
     *
     * @return an empty item stack
     */
    @Override
    public ItemStack getItem() {
        return ItemStack.AIR;
    }
}
