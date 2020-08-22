package de.icevizion.aves.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import de.icevizion.aves.inventory.events.ClickEvent;
import de.icevizion.aves.item.ItemBuilder;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryLayout {

	private final Map<Integer, ItemBuilder> items;
	private final Map<Integer, Consumer<ClickEvent>> clickEvents;

	public InventoryLayout() {
		items = Maps.newHashMap();
		clickEvents = Maps.newHashMap();
	}

	public Map<Integer, ItemBuilder> getItems() {
		return ImmutableMap.copyOf(items);
	}

	public Map<Integer, Consumer<ClickEvent>> getClickEvents() {
		return ImmutableMap.copyOf(clickEvents);
	}

	public void setItem(int slot, ItemBuilder itemBuilder, Consumer<ClickEvent> clickEvent) {
		setItem(slot, itemBuilder);
		clickEvents.put(slot, clickEvent);
	}

	public void setItem(int slot, ItemBuilder itemBuilder) {
		items.put(slot, itemBuilder);
	}
}
