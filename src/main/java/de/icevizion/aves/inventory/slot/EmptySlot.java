package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class EmptySlot implements ISlot {

    @Override
    public @NotNull ISlot setClick(@NotNull InventoryClick slot) {
        throw new UnsupportedOperationException("Cannot set a click on an empty slot");
    }

    @Override
    public @NotNull ISlot setItemStack(ItemStack itemStack) {
        throw new UnsupportedOperationException("Cannot set an item on an empty slot");
    }

    @Override
    public @NotNull InventoryClick getClick() {
        return InventoryConstants.CANCEL_CLICK;
    }

    @Override
    public ItemStack getItem() {
        return ItemStack.AIR;
    }
}
