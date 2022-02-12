package de.icevizion.aves.inventory.slot;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

import java.util.function.Consumer;

/**
 * Represents a slot in an {@link net.minestom.server.inventory.Inventory}.
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0.0
 * @since 1.0.12
 */
public interface ISlot extends Cloneable {

    /**
     * Add a new Listener to the slot.
     * @param clickListener The 'listener' to set
     */
    ISlot setClickListener(Consumer<InventoryPreClickEvent> clickListener);

    /**
     * Returns the {@link Consumer} which includes the {@link InventoryPreClickEvent}.
     * @return the given consumer
     */
    Consumer<InventoryPreClickEvent> getClickListener();

    /**
     * Returns the given {@link ItemStack} from the slot.
     * @return the stack from the slot
     */
    ItemStack getItem();

    /**
     * Returns a cloned {@link ISlot} object.
     * @return the cloned object
     */
    ISlot clone();
}
