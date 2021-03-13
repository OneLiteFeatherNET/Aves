package de.icevizion.aves.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class Slot implements ISlot {

    protected Consumer<InventoryClickEvent> clickListener;
    protected boolean draggable;

    public Slot() {
    }

    public Slot(Consumer<InventoryClickEvent> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public boolean isDraggable() {
        return draggable;
    }

    @Override
    public ISlot setDraggable(boolean enabled) {
        draggable = enabled;
        return this;
    }

    @Override
    public Consumer<InventoryClickEvent> getClickListener() {
        return clickListener;
    }

    @Override
    public ISlot setClickListener(Consumer<InventoryClickEvent> clickListener) {
        this.clickListener = clickListener;
        return this;
    }
}
