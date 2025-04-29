package net.theevilreaper.aves.inventory;

import net.theevilreaper.aves.inventory.holder.InventoryHolder;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.trait.InventoryEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handles the {@link EventListener} creation for some {@link net.minestom.server.event.trait.InventoryEvent}'s.
 * The class reduces some duplicated code parts in the inventory system.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
sealed interface InventoryListenerHandler permits BaseInventoryBuilderImpl {

    EventNode<InventoryEvent> NODE = EventNode.type("inventories", EventFilter.INVENTORY);

    /**
     * Register the a {@link InventoryOpenEvent} and {@link InventoryCloseEvent} listener instance to the event node.
     *
     * @param eventNode     the {@link EventNode} for the events
     * @param openListener  the instance of the {@link EventListener} for the {@link InventoryOpenEvent}
     * @param closeListener the instance of the {@link EventListener} for the {@link InventoryCloseEvent}
     */
    default void register(
            @NotNull EventNode<InventoryEvent> eventNode,
            @Nullable EventListener<InventoryOpenEvent> openListener,
            @Nullable EventListener<InventoryCloseEvent> closeListener) {
        if (openListener != null) {
            eventNode.addListener(openListener);
        }

        if (closeListener != null) {
            eventNode.addListener(closeListener);
        }
    }

    /**
     * Unregister the instance of an {@link InventoryOpenEvent} and {@link InventoryCloseEvent}.
     *
     * @param eventNode     the {@link EventNode} for the events
     * @param openListener  the instance of the {@link EventListener} for the {@link InventoryOpenEvent}
     * @param closeListener the instance of the {@link EventListener} for the {@link InventoryCloseEvent}
     */
    default void unregister(@NotNull EventNode<InventoryEvent> eventNode,
                            @Nullable EventListener<InventoryOpenEvent> openListener,
                            @Nullable EventListener<InventoryCloseEvent> closeListener) {
        if (openListener != null) {
            eventNode.removeListener(openListener);
        }

        if (closeListener != null) {
            eventNode.removeListener(closeListener);
        }
    }

    /**
     * Checks if the given listener can be registered or not.
     * Please note that the listener only checks if the given reference to the listeners are null and
     * not if the reference is registered in the node
     *
     * @param openListener  the instance to the {@link EventListener} for the {@link InventoryOpenEvent}
     * @param closeListener the instance to the {@link EventListener} for the {@link InventoryCloseEvent}
     */
    default void checkListenerState(EventListener<InventoryOpenEvent> openListener,
                                    EventListener<InventoryCloseEvent> closeListener) {
        if (openListener != null && closeListener != null) {
            throw new IllegalStateException("Can't register listener twice");
        }
    }

    /**
     * Create a new {@link EventListener} for the {@link InventoryOpenEvent}.
     *
     * @param builder The builder to handle the event
     * @param holder  The holder from the inventory
     * @return the created listener
     */
    default EventListener<InventoryOpenEvent> registerOpen(@NotNull InventoryBuilder builder, @NotNull InventoryHolder holder) {
        return EventListener.of(InventoryOpenEvent.class, event -> {
            if (event.getInventory() instanceof CustomInventory customInventory && customInventory.getHolder() == holder) {
                builder.handleOpen(event);
            }
        });
    }

    /**
     * Create a new {@link EventListener} for the {@link InventoryCloseEvent}.
     *
     * @param builder The builder to handle the event
     * @param holder  The holder from the inventory
     * @return the created listener
     */
    default EventListener<InventoryCloseEvent> registerClose(@NotNull InventoryBuilder builder, @NotNull InventoryHolder holder) {
        return EventListener.of(InventoryCloseEvent.class, event -> {
            if (event.getInventory() instanceof CustomInventory customInventory && customInventory.getHolder() == holder) {
                builder.handleClose(event);
            }
        });
    }

    /**
     * Returns an indicator if the open or close function is registered.
     *
     * @return true if the function is registered
     */
    boolean hasOpenFunction();

    /**
     * Returns an indicator if the close or open function is registered.
     *
     * @return true if the function is registered
     */
    boolean hasCloseFunction();
}
