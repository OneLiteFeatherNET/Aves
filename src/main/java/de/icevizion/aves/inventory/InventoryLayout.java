package de.icevizion.aves.inventory;

import com.google.common.collect.ImmutableMap;
import de.icevizion.aves.inventory.events.ClickEvent;
import de.icevizion.aves.item.ItemBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represents a layout where all items are located for an inventory.
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.0.0
 * @since 1.0.6
 */

public class InventoryLayout {

	private final Map<Integer, ItemBuilder> items;
	private final Map<Integer, Consumer<ClickEvent>> clickEvents;

	public InventoryLayout() {
		items = new HashMap<>();
		clickEvents = new HashMap<>();
	}

	/**
	 * Add an item with an given slot to the layout with an {@link ClickEvent}.
	 * @param slot The slot id for the item
	 * @param itemBuilder The inventory builder for the item
	 * @param clickEvent The {@link ClickEvent} for the item
	 */

	public void setItem(int slot, ItemBuilder itemBuilder, Consumer<ClickEvent> clickEvent) {
		setItem(slot, itemBuilder);
		clickEvents.putIfAbsent(slot, clickEvent);
	}

	/**
	 * Add an item with an given slot to the layout.
	 * @param slot The slot id for the item
	 * @param itemBuilder The inventory builder for the item
	 */

	public void setItem(int slot, ItemBuilder itemBuilder) {
		items.putIfAbsent(slot, itemBuilder);
	}

	/**
	 * Returns all registered {@link ItemBuilder} from the layout
	 * @return the underlying map
	 */

	public Map<Integer, ItemBuilder> getItems() {
		return ImmutableMap.copyOf(items);
	}

	/**
	 * Returns all registered click events from the layout.
	 * @return the underlying map
	 */

	public Map<Integer, Consumer<ClickEvent>> getClickEvents() {
		return ImmutableMap.copyOf(clickEvents);
	}
}
