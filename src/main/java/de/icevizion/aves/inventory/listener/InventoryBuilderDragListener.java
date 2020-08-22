package de.icevizion.aves.inventory.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryBuilderDragListener implements Listener {

	public InventoryBuilderListener inventoryListener;

	public InventoryBuilderDragListener(InventoryBuilderListener inventoryListener) {
		this.inventoryListener = inventoryListener;
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (inventoryListener.getInventoryHolder(event, player).isPresent()) {
			event.setCancelled(true);
		}
	}
}
