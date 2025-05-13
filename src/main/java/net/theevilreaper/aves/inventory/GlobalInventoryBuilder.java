package net.theevilreaper.aves.inventory;

import net.theevilreaper.aves.inventory.holder.InventoryHolderImpl;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * The {@link GlobalInventoryBuilder} builds an inventory which can be used in a global context.
 * That means that the inventory is related to all player's on the server and not bound to a single player.
 *
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("java:S3252")
public class GlobalInventoryBuilder extends BaseInventoryBuilderImpl {

    private Component titleComponent;
    private CustomInventory inventory;

    /**
     * Creates a new instance from the builder with the given parameter values.
     *
     * @param title the title as {@link Component }for the inventory
     * @param type  the {@link InventoryType} for the inventory
     */
    public GlobalInventoryBuilder(@NotNull Component title, @NotNull InventoryType type) {
        super(type);
        this.titleComponent = title;
        this.holder = new InventoryHolderImpl(this);
    }

    /**
     * Updates the title for the inventory
     *
     * @param titleComponent the new title as {@link Component} to set
     */
    public void setTitleComponent(@NotNull Component titleComponent) {
        this.titleComponent = titleComponent;
        this.updateTitle(inventory, titleComponent);
    }

    /**
     * Returns the inventory with the current state of the items.
     *
     * @param ignored can be ignored
     * @return the underlying inventory
     */
    @Override
    public Inventory getInventory(Locale ignored) {
        if (dataLayoutValid && inventoryLayoutValid && inventory != null)
            return inventory;
        updateInventory();
        return inventory;
    }

    /**
     * Indicates if an inventory is currently open by a player.
     *
     * @return True if a player has the inventory open otherwise false
     */
    @Override
    protected boolean isOpen() {
        return inventory != null && !inventory.getViewers().isEmpty();
    }

    /**
     * Updates the inventory content if something received a change.
     */
    @Override
    protected void updateInventory() {
        boolean applyLayout = !inventoryLayoutValid;
        if (inventory == null) {
            this.inventory = new CustomInventory(this.holder, type, titleComponent);
            applyLayout = true;
        }
        updateInventory(inventory, titleComponent, null, applyLayout);
        updateViewer(inventory);
    }

    /**
     * Applies the data layout to the inventory, if the specific layout is set.
     */
    @Override
    protected void applyDataLayout() {
        synchronized (this) {
            if (getDataLayout() == null) return;
            LOGGER.info("Applying data layouts");
            ItemStack[] contents = inventory.getItemStacks();
            getDataLayout().applyLayout(contents, null);
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] == null) {
                    this.inventory.setItemStack(i, ItemStack.AIR);
                    continue;
                }
                if (contents[i].material() == Material.AIR) continue;
                this.inventory.setItemStack(i, contents[i]);
            }
            this.dataLayoutValid = true;
            updateViewer(inventory);
        }
    }
}
