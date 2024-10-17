package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.holder.InventoryHolder;
import de.icevizion.aves.inventory.holder.InventoryHolderImpl;
import net.minestom.server.MinecraftServer;
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

    static {
        MinecraftServer.getGlobalEventHandler().addChild(NODE);
    }

    protected InventoryHolder holder;
    protected EventListener<InventoryCloseEvent> closeListener;
    protected EventListener<InventoryOpenEvent> openListener;

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
        if (this.openFunction != null) {
            this.openListener = registerOpen(this, holder);
        }

        if (this.closeFunction != null) {
            this.closeListener = registerClose(this, holder);
        }

        this.register(NODE, openListener, closeListener);
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
        this.unregister(NODE, openListener, closeListener);
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
