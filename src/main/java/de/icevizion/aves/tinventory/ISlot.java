package de.icevizion.aves.tinventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public interface ISlot {

    boolean isDraggable();
    ISlot setDraggable(boolean enabled);

    ItemStack getItem();

    Consumer<InventoryClickEvent> getClickListener();
    ISlot setClickListener(Consumer<InventoryClickEvent> clickListener);
}
