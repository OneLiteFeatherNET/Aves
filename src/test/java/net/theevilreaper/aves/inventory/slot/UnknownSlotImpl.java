package net.theevilreaper.aves.inventory.slot;

import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnknownSlotImpl implements ISlot {
    @Override
    public @NotNull ISlot setClick(@NotNull InventoryClick slot) {
        return this;
    }

    @Override
    public @NotNull ISlot setItemStack(@Nullable ItemStack itemStack) {
        return this;
    }

    @Override
    public @NotNull InventoryClick getClick() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }
}
