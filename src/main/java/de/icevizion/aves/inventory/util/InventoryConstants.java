package de.icevizion.aves.inventory.util;

import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.inventory.slot.EmptySlot;
import net.minestom.server.event.trait.CancellableEvent;

import java.util.function.Consumer;

/**
 * Contains some constant which are used for inventories.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
public final class InventoryConstants {

    public static final InventoryClick CANCEL_CLICK = (player, clickType, slotID, condition) -> condition.setCancel(true);
    public static final Consumer<CancellableEvent> CANCELLABLE_EVENT = event -> event.setCancelled(true);
    public static final EmptySlot BLANK_SLOT = new EmptySlot();
    public static final int INVENTORY_WIDTH = 9;
    public static final int INVALID_SLOT_ID = -999;

    /**
     * Private constructor without any usage.
     */
    private InventoryConstants() {}
}
