package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.function.InventoryClick;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class Slot implements ISlot {

    protected InventoryClick inventoryClick;
    protected ItemStack itemStack;

    protected Slot() { }

    protected Slot(InventoryClick inventoryClick) {
        this.inventoryClick = inventoryClick;
    }

    protected Slot(InventoryClick inventoryClick, ItemStack itemStack) {
        this.inventoryClick = inventoryClick;
        this.itemStack = itemStack;
    }

    @Override
    public ISlot setClick(@NotNull InventoryClick slot) {
        this.inventoryClick = slot;
        return this;
    }

    @Override
    public ISlot setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

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
