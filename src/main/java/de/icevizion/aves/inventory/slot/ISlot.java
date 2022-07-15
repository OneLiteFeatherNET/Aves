package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.function.InventoryClick;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a slot in an {@link net.minestom.server.inventory.Inventory}.
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0.0
 * @since 1.0.12
 */
public interface ISlot extends Cloneable {

    /**
     * Set's a new {@link InventoryClick} reference to the slot
     * @param slot The slot to set
     */
    ISlot setClick(@NotNull InventoryClick slot);


    ISlot setItemStack(@Nullable ItemStack itemStack);

    /**
     * Returns the given inventory click from the slot
     * @return the given click
     */
    @Nullable
    InventoryClick getClick();

    /**
     * Returns the given {@link ItemStack} from the slot.
     * @return the stack from the slot
     */
    ItemStack getItem();

    /**
     * Returns a cloned {@link ISlot} object.
     * @return the cloned object
     */
    ISlot clone();
}
