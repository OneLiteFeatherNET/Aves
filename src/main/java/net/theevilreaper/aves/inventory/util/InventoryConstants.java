package net.theevilreaper.aves.inventory.util;

import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.theevilreaper.aves.inventory.slot.EmptySlot;
import net.minestom.server.event.trait.CancellableEvent;

import java.util.function.Consumer;

/**
 * Contains some constant which are used for inventories.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
public final class InventoryConstants {

    public static final InventoryClick CANCEL_CLICK = (player, slot, click) -> ClickHolder.cancelClick();
    @Deprecated(forRemoval = true, since = "Not needed anymore due to the changes to the inventory system")
    public static final Consumer<CancellableEvent> CANCELLABLE_EVENT = event -> event.setCancelled(true);
    public static final EmptySlot BLANK_SLOT = new EmptySlot();
    public static final int INVENTORY_WIDTH = 9;
    public static final int INVALID_SLOT_ID = -999;

    /**
     * Private constructor without any usage.
     */
    private InventoryConstants() {}
}
