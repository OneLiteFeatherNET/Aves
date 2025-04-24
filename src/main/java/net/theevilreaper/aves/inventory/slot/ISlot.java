package net.theevilreaper.aves.inventory.slot;

import net.theevilreaper.aves.inventory.InventorySlot;
import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a slot in an {@link net.minestom.server.inventory.Inventory}.
 * @author Patrick Zdarsky / Rxcki
 * @version 1.1.0
 * @since 1.0.12
 */
public interface ISlot {

    /**
     * Creates a copy of a given slot.
     * The method checks which slot implementation is present and call then the specific copy method.
     * This method only work with the given implementations at the moment. Each custom implementation will not work!
     * @param iSlot the slot to copy
     * @return the created if the slot matches with an implementation otherwise null
     */
    @Contract(pure = true)
    static @Nullable ISlot of(@NotNull ISlot iSlot) {
        if (iSlot instanceof InventorySlot inventorySlot) {
            return InventorySlot.of(inventorySlot);
        }

        if (iSlot instanceof TranslatedSlot translatedSlot) {
            return TranslatedSlot.of(translatedSlot);
        }

        return null;
    }

    /**
     * Set's a new {@link InventoryClick} reference to the slot
     * @param slot The slot to set
     */
    @NotNull ISlot setClick(@NotNull InventoryClick slot);

    /**
     * Set a new {@link ItemStack} to the slot.
     * @param itemStack the stack to set
     */
    @NotNull ISlot setItemStack(@Nullable ItemStack itemStack);

    /**
     * Returns the given inventory click from the slot
     * @return the given click
     */
    @NotNull InventoryClick getClick();

    /**
     * Returns the given {@link ItemStack} from the slot.
     * @return the stack from the slot
     */
    ItemStack getItem();
}
