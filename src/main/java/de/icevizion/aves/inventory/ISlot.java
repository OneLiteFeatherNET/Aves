package de.icevizion.aves.inventory;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public interface ISlot extends Cloneable {

    ItemStack getItem();

    Consumer<InventoryPreClickEvent> getClickListener();

    ISlot setClickListener(Consumer<InventoryPreClickEvent> clickListener);

    ISlot clone();
}
