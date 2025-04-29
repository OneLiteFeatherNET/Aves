package net.theevilreaper.aves.inventory.function;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.theevilreaper.aves.inventory.InventoryBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * With the interface you can easily tell what should happen when an inventory is closed.
 * You can set an implementation of the interface in the {@link InventoryBuilder}
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.1.0
 **/
@FunctionalInterface
public interface CloseFunction {

    /**
     * Handles what happen when the inventory will be closed
     * @param event The {@link InventoryCloseEvent} which is fired
     */
    void onClose(@NotNull InventoryCloseEvent event);
}