package de.icevizion.aves.inventory.listener;

import de.icevizion.aves.inventory.InventoryBuilder;
import de.icevizion.aves.inventory.events.CloseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryBuilderCloseListener implements Listener {

	private final InventoryBuilderListener inventoryListener;

	public InventoryBuilderCloseListener(InventoryBuilderListener inventoryListener) {
		this.inventoryListener = inventoryListener;
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (event.getInventory() == player.getInventory()) {
			return;
		}

		Optional<InventoryBuilder.Holder> optionalHolder = inventoryListener.getInventoryHolder(event,
				player);
		if (!optionalHolder.isPresent()) {
			return;
		}

		InventoryBuilder.Holder holder = optionalHolder.get();
		CloseEvent closeEvent = new CloseEvent(inventoryListener.getCloudService(), event);
		boolean denyInventoryClose = holder.getInventoryBuilder().onInventoryClose(closeEvent);
		if (denyInventoryClose) {
			event.getPlayer().openInventory(holder.getInventory());
		}
	}
}
