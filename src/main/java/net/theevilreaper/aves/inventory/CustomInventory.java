package net.theevilreaper.aves.inventory;

import net.theevilreaper.aves.inventory.holder.InventoryHolder;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a normal inventory which can hold an implementation of a {@link InventoryHolder}.
 * The class should only be used in the internal structure of the inventory api.
 * The api uses the class to reduce the complexity to check which inventory should receive the events on inventories.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
public class CustomInventory extends Inventory {

    private final InventoryHolder holder;

    /**
     * Creates a new instance from the {@link CustomInventory}.
     * @param holder the holder of the inventory
     * @param inventoryType the type for the inventory
     * @param title the title as {@link Component}
     */
    public CustomInventory(InventoryHolder holder, @NotNull InventoryType inventoryType, @NotNull Component title) {
        super(inventoryType, title);
        this.holder = holder;
    }

    /**
     * Checks if the inventory has any viewers.
     * @return true if the inventory has viewers
     */
    public boolean hasViewers() {
        return !this.getViewers().isEmpty();
    }

    /**
     * Returns the holder instance from the inventory.
     * @return the underlying instance to the holder
     */
    @Nullable
    public InventoryHolder getHolder() {
        return holder;
    }
}