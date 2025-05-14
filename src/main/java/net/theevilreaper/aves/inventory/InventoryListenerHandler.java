package net.theevilreaper.aves.inventory;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.click.Click;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.exception.ListenerStateException;
import net.theevilreaper.aves.inventory.holder.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * Handles the {@link EventListener} creation for some {@link InventoryEvent}'s.
 * The class reduces some duplicated code parts in the inventory system.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
sealed interface InventoryListenerHandler permits BaseInventoryBuilderImpl {

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
            @Nullable EventListener<InventoryCloseEvent> closeListener,
            @Nullable EventListener<InventoryPreClickEvent> clickListener
    ) {
        if (openListener != null) {
            eventNode.addListener(openListener);
        }

        if (closeListener != null) {
            eventNode.addListener(closeListener);
        }

        if (clickListener != null) {
            eventNode.addListener(clickListener);
        }
    }

    /**
     * Unregister the instance of an {@link InventoryOpenEvent} and {@link InventoryCloseEvent}.
     *
     * @param eventNode     the {@link EventNode} for the events
     * @param openListener  the instance of the {@link EventListener} for the {@link InventoryOpenEvent}
     * @param closeListener the instance of the {@link EventListener} for the {@link InventoryCloseEvent}
     */
    default void unregister(
            @NotNull EventNode<InventoryEvent> eventNode,
            @Nullable EventListener<InventoryOpenEvent> openListener,
            @Nullable EventListener<InventoryCloseEvent> closeListener,
            @Nullable EventListener<InventoryPreClickEvent> clickListener
    ) {
        if (openListener != null) {
            eventNode.removeListener(openListener);
        }

        if (closeListener != null) {
            eventNode.removeListener(closeListener);
        }

        if (clickListener != null) {
            eventNode.removeListener(clickListener);
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
    default void checkListenerState(
            @Nullable EventListener<InventoryOpenEvent> openListener,
            @Nullable EventListener<InventoryCloseEvent> closeListener,
            @Nullable EventListener<InventoryPreClickEvent> clickListener
    ) {
        // Since clickListener is always present after registration, check it first
        if (clickListener != null) {
            throw new ListenerStateException("Can't register click listener twice");
        }
        // Keep the original check as a fallback
        if (openListener != null && closeListener != null) {
            throw new ListenerStateException("The open and close listener can't be registered twice");
        }
    }

    /**
     * Create a new {@link EventListener} for the {@link InventoryOpenEvent}.
     *
     * @param builder The builder to handle the event
     * @param holder  The holder from the inventory
     * @return the created listener
     */
    default EventListener<InventoryOpenEvent> registerOpen(
            @NotNull InventoryBuilder builder,
            @NotNull InventoryHolder holder
    ) {
        return EventListener.of(InventoryOpenEvent.class, event -> {
            if (event.getInventory() instanceof CustomInventory customInventory && customInventory.getHolder() == holder) {
                builder.handleOpen(event);
            }
        });
    }

    /**
     * Create a new {@link EventListener} for the {@link InventoryClickEvent}.
     *
     * @param builder The builder to handle the event
     * @param holder  The holder from the inventory
     * @return the created listener
     */
    default EventListener<InventoryPreClickEvent> registerClick(
            @NotNull InventoryBuilder builder,
            @NotNull InventoryHolder holder
    ) {
        return EventListener.of(InventoryPreClickEvent.class, event -> {
            if (event.getInventory() instanceof CustomInventory customInventory && customInventory.getHolder() == holder) {
                ClickHolder click = builder.inventoryClick.onClick(event.getPlayer(), event.getSlot(), event.getClick());

                switch (click) {
                    case ClickHolder.CancelClick ignored1 -> event.setCancelled(true);
                    case ClickHolder.MinestomClick(@NotNull Click minestomClick) -> event.setClick(minestomClick);
                    case ClickHolder.NOPClick ignored -> {
                        // No operation
                    }
                }
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
    default EventListener<InventoryCloseEvent> registerClose(
            @NotNull InventoryBuilder builder,
            @NotNull InventoryHolder holder
    ) {
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
