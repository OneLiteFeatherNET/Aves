package de.icevizion.aves.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import de.icevizion.aves.inventory.events.ClickEvent;
import de.icevizion.aves.inventory.events.CloseEvent;
import de.icevizion.aves.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryBuilder {

	private static final InventoryLayout EMPTY_LAYOUT = new InventoryLayout();

	private final String title;
	private final Map<Integer, ItemBuilder> items;
	private final Map<Integer, ItemBuilder> backGroundItems;
	private final Map<Integer, Consumer<ClickEvent>> clickEvents;
	private Inventory inventory;
	private InventoryRows rows;
	private boolean firstDraw;
	private boolean drawOnce;

	public InventoryBuilder(String title, InventoryRows rows) {
		this(title, rows, EMPTY_LAYOUT);
	}

	public InventoryBuilder(String title, InventoryRows rows, InventoryLayout layout) {
		this.title = title;
		this.rows = rows;

		items = Maps.newHashMap();
		backGroundItems = Maps.newHashMap(layout.getItems());
		clickEvents = Maps.newHashMap(layout.getClickEvents());

		firstDraw = true;
		buildInventory();
	}

	public void draw() {

	}

	public boolean onInventoryClose(CloseEvent closeEvent) {
		return false;
	}

	public final InventoryRows getRows() {
		return rows;
	}

	public final boolean isFirstDraw() {
		return firstDraw;
	}

	public final Map<Integer, Consumer<ClickEvent>> getClickEvents() {
		return ImmutableMap.copyOf(clickEvents);
	}

	public final void setItem(int slot, ItemBuilder itemBuilder, Consumer<ClickEvent> clickEvent) {
		setItem(slot, itemBuilder);
		clickEvents.put(slot, clickEvent);
	}

	public final void setItem(int slot, ItemBuilder itemBuilder) {
		items.put(slot, itemBuilder);
	}

	public final void setBackgroundItem(int slot, ItemBuilder itemBuilder) {
		backGroundItems.put(slot, itemBuilder);
	}

	public final void setBackgroundItems(int fromIndex, int toIndex, ItemBuilder itemBuilder) {
		for (int i = fromIndex; i <= toIndex; i++) {
			setBackgroundItem(i, itemBuilder);
		}
	}

	public final void setInventorySize(InventoryRows rows) {
		if (this.rows == rows) {
			return;
		}

		this.rows = rows;
		Inventory oldInventory = inventory;
		inventory = Bukkit.createInventory(new Holder(this), rows.getSize(), title);

		items.clear();
		backGroundItems.clear();
		clickEvents.clear();

		firstDraw = true;

		draw();
		drawItems();

		//TODO fix Concurrent Modification Exception
		oldInventory.getViewers().forEach(humanEntity -> humanEntity.openInventory(inventory));
		oldInventory.clear();
	}

	public final void setFirstDraw(boolean firstDraw) {
		this.firstDraw = firstDraw;
	}

	public final void setDrawOnce(boolean drawOnce) {
		this.drawOnce = drawOnce;
	}

	public final void drawItems() {
		inventory.clear();
		firstDraw = false;

		backGroundItems.forEach((slot, item) -> inventory.setItem(slot, item.build()));

		items.forEach(
				(slot, item) -> inventory.setItem(slot, Objects.isNull(item) ? null : item.build()));
	}

	protected final Inventory getInventory() {
		return inventory;
	}

	protected final void buildInventory() {
		buildInventory(false);
	}

	protected final void buildInventory(boolean ignoreDrawOnce) {
		if (Objects.isNull(inventory)) {
			inventory = Bukkit.createInventory(new Holder(this), rows.getSize(), title);
		}

		if (!ignoreDrawOnce && (!drawOnce || firstDraw)) {
			draw();
		}

		drawItems();
	}

	public static final class Holder implements InventoryHolder {

		private final InventoryBuilder inventoryBuilder;

		private Holder(InventoryBuilder inventoryBuilder) {
			this.inventoryBuilder = inventoryBuilder;
		}

		@Override
		public Inventory getInventory() {
			return inventoryBuilder.inventory;
		}

		public InventoryBuilder getInventoryBuilder() {
			return inventoryBuilder;
		}
	}
}
