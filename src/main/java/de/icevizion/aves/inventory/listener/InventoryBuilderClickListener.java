package de.icevizion.aves.inventory.listener;

import de.icevizion.aves.inventory.InventoryBuilder;
import de.icevizion.aves.inventory.events.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryBuilderClickListener implements Listener {

	private final InventoryBuilderListener inventoryListener;

	public InventoryBuilderClickListener(InventoryBuilderListener inventoryListener) {
		this.inventoryListener = inventoryListener;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getClickedInventory() == null
				|| event.getClickedInventory() == player.getInventory()) {
			return;
		}

		var optionalHolder = inventoryListener.getInventoryHolder(event, player);
		if (optionalHolder.isEmpty()) {
			return;
		}

		event.setCancelled(true);
		InventoryBuilder.Holder holder = optionalHolder.get();
		var clickEventConsumer = holder.getInventoryBuilder()
				.getClickEvents().get(event.getRawSlot());
		if (Objects.nonNull(clickEventConsumer)) {
			ClickEvent clickEvent = new ClickEvent(event);
			clickEventConsumer.accept(clickEvent);
		}
	}
}
