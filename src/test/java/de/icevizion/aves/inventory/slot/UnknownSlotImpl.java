package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.function.InventoryClick;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnknownSlotImpl implements ISlot {
    @Override
    public ISlot setClick(@NotNull InventoryClick slot) {
        return null;
    }

    @Override
    public ISlot setItemStack(@Nullable ItemStack itemStack) {
        return null;
    }

    @Override
    public @Nullable InventoryClick getClick() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }
}
