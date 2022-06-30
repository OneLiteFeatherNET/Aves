package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.function.InventoryClick;
import org.jetbrains.annotations.NotNull;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class Slot implements ISlot {

    protected InventoryClick slot;

    public Slot() { }

    public Slot(InventoryClick slot) {
        this.slot = slot;
    }

    @Override
    public ISlot setClick(@NotNull InventoryClick slot) {
        this.slot = slot;
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
