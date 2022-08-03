package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.holder.InventoryHolder;
import de.icevizion.aves.inventory.holder.InventoryHolderImpl;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0.0
 * @since 1.0.0
 */
public non-sealed class GlobalInventoryBuilder extends InventoryBuilder implements InventoryListenerHandler {

    static {
        MinecraftServer.getGlobalEventHandler().addChild(NODE);
    }

    private final Component titleComponent;
    private CustomInventory inventory;

    private InventoryHolder holder;

    private EventListener<InventoryCloseEvent> closeListener;
    private EventListener<InventoryOpenEvent> openListener;

    public GlobalInventoryBuilder(@NotNull String title, @NotNull InventoryType type) {
        super(type);
        this.titleComponent = Component.text(title);
        this.holder = new InventoryHolderImpl(this);
    }

    public GlobalInventoryBuilder(@NotNull Component title, @NotNull InventoryType type) {
        super(type);
        this.titleComponent = title;
        this.holder = new InventoryHolderImpl(this);
    }

    @Override
    public void register() {
        this.checkListenerState(this.openListener, this.closeListener);
        if (this.openFunction != null) {
            this.openListener = registerOpen(this, holder);
        }

        if (this.closeFunction != null) {
            this.closeListener = registerClose(this, holder);
        }

        this.register(NODE, openListener, closeListener);
    }

    @Override
    public void unregister() {
        if (!getInventory().getViewers().isEmpty()) {
            for (Player viewer : getInventory().getViewers()) {
                viewer.closeInventory();
            }
        }
        this.unregister(NODE, openListener, closeListener);
        this.holder = null;
    }

    @Override
    public Inventory getInventory(Locale ignored) {
        if (dataLayoutValid && inventoryLayoutValid && inventory != null)
            return inventory;
        updateInventory();
        return inventory;
    }

    /**
     * Indicates if an inventory is currently open by a player.
     * @return True if a player has the inventory open otherwise false
     */
    @Override
    protected boolean isOpen() {
        return inventory != null && !inventory.getViewers().isEmpty();
    }

    @Override
    protected void updateInventory() {
        boolean applyLayout = !inventoryLayoutValid;
        if (inventory == null) {
            this.inventory = new CustomInventory(this.holder, type, titleComponent);
            this.inventory.addInventoryCondition(inventoryCondition);
            applyLayout = true;
        }

        updateInventory(inventory, titleComponent, null, null, applyLayout);
        updateViewer(inventory);
    }

    @Override
    protected void applyDataLayout() {
        synchronized (this) {
            if (getDataLayout() != null) {
                LOGGER.info("Applying data layouts " + getDataLayout());
                var contents = inventory.getItemStacks();
                getDataLayout().applyLayout(contents, null, null);
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i].material() == Material.AIR) continue;
                    this.inventory.setItemStack(i, contents[i]);
                }
                updateViewer(inventory);
            }
        }
    }
}
