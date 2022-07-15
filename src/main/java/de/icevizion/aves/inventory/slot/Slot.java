package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.item.Item;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class Slot implements ISlot {

    protected InventoryClick slot;
    protected ItemStack itemStack;

    public Slot() { }

    public Slot(InventoryClick slot) {
        this.slot = slot;
    }

    public Slot(InventoryClick slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    @Override
    public ISlot setClick(@NotNull InventoryClick slot) {
        this.slot = slot;
        return this;
    }

    @Override
    public ISlot setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Override
    public InventoryClick getClick() {
        return this.slot;
    }

    @Override
    public ISlot clone() {
        try {
            return (ISlot) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("This should never happen", e);
        }
    }

    /**
     * Returns a text representation of the slot.
     * @return the slot as string
     */
    @Override
    public String toString() {
        return "Slot{" + "clickListener=" + slot + '}';
    }
}
