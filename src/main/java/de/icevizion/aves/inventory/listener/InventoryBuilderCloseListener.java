package de.icevizion.aves.inventory.listener;

import de.icevizion.aves.inventory.InventoryBuilder;
import de.icevizion.aves.inventory.events.CloseEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryBuilderCloseListener implements Listener {

	private final Plugin plugin;
	private final InventoryBuilderListener inventoryListener;

	public InventoryBuilderCloseListener(Plugin plugin, InventoryBuilderListener inventoryListener) {
		this.plugin = plugin;
		this.inventoryListener = inventoryListener;
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (event.getInventory() == player.getInventory()) {
			return;
		}

		var optionalHolder = inventoryListener.getInventoryHolder(event, player);
		if (optionalHolder.isEmpty()) {
			return;
		}

		InventoryBuilder.Holder holder = optionalHolder.get();
		CloseEvent closeEvent = new CloseEvent(event);
		holder.getInventoryBuilder().onInventoryClose(closeEvent);

		if (closeEvent.isCancelled()) {
			Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(holder.getInventory()), 3);
		}
	}
}