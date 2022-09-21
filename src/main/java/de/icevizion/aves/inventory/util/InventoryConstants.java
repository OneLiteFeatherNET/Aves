package de.icevizion.aves.inventory.util;

import de.icevizion.aves.inventory.InventorySlot;
import de.icevizion.aves.inventory.function.InventoryClick;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.item.ItemStack;

import java.util.function.Consumer;

/**
 * Contains some constant which are used for inventories
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public final class InventoryConstants {

    public static final InventoryClick CANCEL_CLICK = (player, clickType, slotID, condition) -> condition.setCancel(true);
    public static final Consumer<CancellableEvent> CANCELLABLE_EVENT = event -> event.setCancelled(true);
    public static final InventorySlot EMPTY_SLOT = new InventorySlot(ItemStack.AIR);

    private InventoryConstants() {}
}
