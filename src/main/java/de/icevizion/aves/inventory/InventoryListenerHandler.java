package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.holder.InventoryHolder;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Handles the {@link EventListener} creation for some {@link net.minestom.server.event.trait.InventoryEvent}'s.
 * The class reduces some duplicated code parts in the inventory system.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public interface InventoryListenerHandler {

    /**
     * Create a new {@link EventListener} for the {@link InventoryOpenEvent}.
     * @param builder The builder to handle the event
     * @param holder The holder from the inventory
     * @return the created listener
     */
    default EventListener<InventoryOpenEvent> registerOpen(@NotNull InventoryBuilder builder, @NotNull InventoryHolder holder) {
        return EventListener.of(InventoryOpenEvent.class, event -> {
            if (event.getInventory() instanceof CustomInventory customInventory && customInventory.getHolder() == holder) {
                builder.handleOpen(event);
            }
        });
    }

    /**
     * Create a new {@link EventListener} for the {@link InventoryCloseEvent}.
     * @param builder The builder to handle the event
     * @param holder The holder from the inventory
     * @return the created listener
     */
    default EventListener<InventoryCloseEvent> registerClose(@NotNull InventoryBuilder builder, @NotNull InventoryHolder holder) {
        return EventListener.of(InventoryCloseEvent.class, event -> {
            if (event.getInventory() instanceof CustomInventory customInventory && customInventory.getHolder() == holder) {
                builder.handleClose(event);
            }
        });
    }
}
