package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.events.ClickEvent;
import de.icevizion.aves.inventory.events.CloseEvent;
import de.icevizion.aves.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryBuilder {

	private static final InventoryLayout EMPTY_LAYOUT = new InventoryLayout();

	private final InventoryLayout layout;
	private final Holder holder;
	private final Map<Integer, ItemBuilder> items;
	private final Map<Integer, ItemBuilder> backGroundItems;
	private final Map<Integer, Consumer<ClickEvent>> clickEvents;
	private Inventory inventory;
	private String title;
	private InventoryRows rows;
	private boolean firstDraw;
	private boolean drawOnce;

	public InventoryBuilder(String title, InventoryRows rows) {
		this(title, rows, EMPTY_LAYOUT);
	}

	public InventoryBuilder(String title, InventoryRows rows, InventoryLayout layout) {
		this.title = title;
		this.rows = rows;
		this.layout = layout;

		holder = new Holder(this);
		items = new HashMap<>();
		backGroundItems = new HashMap<>(layout.getItems());
		clickEvents = new HashMap<>(layout.getClickEvents());
		firstDraw = true;
	}

	public void draw() { }

	public void onInventoryClose(CloseEvent closeEvent) { }

	public final void clearItems() {
		items.clear();
		clickEvents.clear();
		clickEvents.putAll(layout.getClickEvents());
	}

	public final void setItem(int slot, ItemBuilder itemBuilder, Consumer<ClickEvent> clickEvent) {
		setItem(slot, itemBuilder);
		clickEvents.put(slot, clickEvent);
	}

	public final void setItem(int slot, ItemBuilder itemBuilder) {
		items.put(slot, itemBuilder);
	}

	public final void removeItem(int slot) {
		items.remove(slot);
		clickEvents.remove(slot);
	}

	public final void setBackgroundItem(int slot, ItemBuilder itemBuilder) {
		backGroundItems.put(slot, itemBuilder);
	}

	public final void setBackgroundItems(int fromIndex, int toIndex, ItemBuilder itemBuilder) {
		int maxSize = rows.getSize() - 1;
		if (toIndex > maxSize) {
			toIndex = maxSize;
		}

		for (int i = fromIndex; i <= toIndex; i++) {
			setBackgroundItem(i, itemBuilder);
		}
	}

	/**
	 * Change the size of the inventory. The inventory will be rebuild when the size changes.
	 * @param rows The new size of the inventory
	 */

	public final void setInventorySize(InventoryRows rows) {
		if (this.rows == rows) {
			return;
		}

		this.rows = rows;
		rebuildInventory();
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

	/**
	 * Change the title from the inventory. The inventory will be rebuild when the size changes.
	 * @param title The new title for the inventory
	 */

	public void setInventoryTitle(String title) {
		if (this.title.equals(title)) {
			return;
		}

		this.title = title;
		rebuildInventory();
	}

	/**
	 * Builds the inventory.
	 */

	protected final void buildInventory() {
		buildInventory(false);
	}

	protected final void buildInventory(boolean ignoreDrawOnce) {
		if (Objects.isNull(inventory)) {
			inventory = Bukkit.createInventory(holder, rows.getSize(), title);
		}

		if (!ignoreDrawOnce && (!drawOnce || firstDraw)) {
			draw();
		}

		drawItems();
	}

	/**
	 * The method calls the logic to rebuild the inventory
	 */

	private void rebuildInventory() {
		Inventory oldInventory = inventory;
		inventory = Bukkit.createInventory(holder, rows.getSize(), title);

		items.clear();
		backGroundItems.clear();
		clickEvents.clear();

		firstDraw = true;

		draw();
		drawItems();

		if (oldInventory.getViewers().isEmpty()) return;

		//TODO fix Concurrent Modification Exception
		oldInventory.getViewers().forEach(humanEntity -> humanEntity.openInventory(inventory));
		oldInventory.clear();
	}

	/**
	 * Returns the given inventory.
	 * @return The underlying inventory
	 */

	protected final Inventory getInventory() {
		return inventory;
	}

	/**
	 * Returns the amount of rows from the inventory.
	 * @return The enum representation for the row
	 */

	public final InventoryRows getRows() {
		return rows;
	}

	public final boolean isFirstDraw() {
		return firstDraw;
	}

	/**
	 * Returns a map which contains all slots who has a registered {@link ClickEvent}
	 * @return the underlying map
	 */

	public final Map<Integer, Consumer<ClickEvent>> getClickEvents() {
		return clickEvents;
	}

	public final Map<Integer, ItemBuilder> getItems() {
		return items;
	}

	public final ItemBuilder getItem(int slot) {
		return items.get(slot);
	}

	public Holder getHolder() {
		return holder;
	}

	public List<HumanEntity> getViewers() {
		return inventory.getViewers();
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
