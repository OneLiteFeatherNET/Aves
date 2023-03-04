package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.function.InventoryClick;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link Slot} is a basic abstract implementation of the {@link ISlot} interface.
 *
 * @since 1.0.0
 * @version 1.0.1
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class Slot implements ISlot {

    protected InventoryClick inventoryClick;
    protected ItemStack itemStack;

    /**
     * Creates a new instance from the {@link Slot} without any parameters.
     */
    protected Slot() { }

    /**
     * Creates a new instance from the {@link Slot} class.
     * @param inventoryClick the {@link InventoryClick} for the slot
     */
    protected Slot(InventoryClick inventoryClick) {
        this.inventoryClick = inventoryClick;
    }

    /**
     * Creates a new instance from the {@link Slot} class.
     * @param inventoryClick the {@link InventoryClick} for the slot
     * @param itemStack the {@link ItemStack} to set
     */
    protected Slot(InventoryClick inventoryClick, ItemStack itemStack) {
        this.inventoryClick = inventoryClick;
        this.itemStack = itemStack;
    }

    /**
     * Set a new reference from the {@link InventoryClick} to the slot.
     * @param slot The slot to set
     * @return the slot reference
     */
    @Override
    public ISlot setClick(@NotNull InventoryClick slot) {
        this.inventoryClick = slot;
        return this;
    }

    /**
     * Set a new reference from an {@link ItemStack} to the slot.
     * @param itemStack the stack to set
     * @return the slot reference
     */
    @Override
    public ISlot setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    /**
     * Returns the given {@link InventoryClick} reference.
     * @return the given reference or null when no listener is set
     */
    @Override
    public InventoryClick getClick() {
        return this.inventoryClick;
    }

    /**
     * Returns a text representation of the slot.
     * @return the slot as string
     */
    @Override
    public String toString() {
        return "Slot{" + "clickListener=" + inventoryClick + '}';
    }
}
