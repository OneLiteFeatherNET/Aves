package de.icevizion.aves.inventory.slot;

import net.minestom.server.event.inventory.InventoryPreClickEvent;

import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class Slot implements ISlot {

    protected Consumer<InventoryPreClickEvent> clickListener;

    public Slot() { }

    public Slot(Consumer<InventoryPreClickEvent> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public Consumer<InventoryPreClickEvent> getClickListener() {
        return clickListener;
    }

    @Override
    public ISlot setClickListener(Consumer<InventoryPreClickEvent> clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    @Override
    public ISlot clone() {
        try {
            return (ISlot) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("This should never happen", e);
        }
    }

    @Override
    public String toString() {
        return "Slot{" + "clickListener=" + clickListener + '}';
    }
}
