package net.theevilreaper.aves.inventory;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.theevilreaper.aves.inventory.holder.InventoryHolder;
import net.theevilreaper.aves.inventory.holder.InventoryHolderImpl;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

/**
 * The class is the basic implementation of the {@link InventoryBuilder} and also represents the middle layer
 * between the {@link InventoryBuilder} and the concrete inventory implementations.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public abstract non-sealed class BaseInventoryBuilderImpl extends InventoryBuilder implements InventoryListenerHandler {

    protected InventoryHolder holder;
    protected EventListener<InventoryCloseEvent> closeListener;
    protected EventListener<InventoryOpenEvent> openListener;
    protected EventListener<InventoryPreClickEvent> clickListener;

    /**
     * Creates a new instance from the {@link BaseInventoryBuilderImpl}.
     *
     * @param type the {@link InventoryType} to set
     */
    protected BaseInventoryBuilderImpl(@NotNull InventoryType type) {
        super(type);
        this.holder = new InventoryHolderImpl(this);
    }

    /**
     * Registers some event listeners into the event graph from the server.
     * The listeners can't be registered twice
     */
    @Override
    public void register() {
        this.checkListenerState(this.openListener, this.closeListener);
        if (this.openFunction == null) {
            this.openListener = registerOpen(this, holder);
        }

        if (this.closeFunction == null) {
            this.closeListener = registerClose(this, holder);
        }

        if (this.clickListener == null) {
            this.clickListener = registerClick(this, holder);
        }

        this.register(this.holder.getInventory().eventNode(), openListener, closeListener, clickListener);
    }

    /**
     * Unregisters all listeners from the event graph.
     * Also, the inventory will be closed for all players who have this is inventory open.
     */
    @Override
    public void unregister() {
        if (!getInventory().getViewers().isEmpty()) {
            for (Player viewer : getInventory().getViewers()) {
                viewer.closeInventory();
            }
        }
        this.unregister(this.holder.getInventory().eventNode(), openListener, closeListener, clickListener);
        this.holder = null;
    }

    /**
     * Returns the holder of the inventory.
     *
     * @return the holder of the inventory
     */
    @Override
    public boolean hasCloseFunction() {
        return this.closeFunction != null;
    }

    /**
     * Returns an indicator if the open function is registered.
     *
     * @return true if the function is registered
     */
    @Override
    public boolean hasOpenFunction() {
        return this.openFunction != null;
    }
}
