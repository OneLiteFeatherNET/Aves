package net.theevilreaper.aves.inventory.function;

import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.theevilreaper.aves.inventory.InventoryBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * With the interface you can easily tell what should happen when an inventory opens.
 * You can set an implementation of the interface in the {@link InventoryBuilder}
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.1.0
 **/
@FunctionalInterface
public interface OpenFunction {

    /**
     * Handles what happen when the inventory will be open
     *
     * @param event The {@link InventoryOpenEvent} which is fired
     */
    void onOpen(@NotNull InventoryOpenEvent event);
}