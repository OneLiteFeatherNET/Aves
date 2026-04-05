package net.theevilreaper.aves.inventory.util;

import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.theevilreaper.aves.inventory.slot.EmptySlot;

/**
 * Contains some constant used for inventories.
 *
 * @author theEvilReaper
 * @version 1.0.1
 * @since 1.2.0
 **/
public final class InventoryConstants {

    public static final InventoryClick CANCEL_CLICK = (player, slot, click, stack, result) -> result.accept(ClickHolder.cancelClick());
    public static final EmptySlot BLANK_SLOT = new EmptySlot();
    public static final int INVENTORY_WIDTH = 9;
    public static final int INVALID_SLOT_ID = -999;

    /**
     * Private constructor without any usage.
     */
    private InventoryConstants() {
    }
}
