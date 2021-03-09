package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.listener.InventoryBuilderClickListener;
import de.icevizion.aves.inventory.listener.InventoryBuilderCloseListener;
import de.icevizion.aves.inventory.listener.InventoryBuilderDragListener;
import de.icevizion.aves.inventory.listener.InventoryBuilderListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * This class contains some methods to open the different inventories available in the system.
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.1.0
 * @since 1.0.6
 */

public class InventoryService {

	public InventoryService(Plugin plugin) {
		var builderListener = new InventoryBuilderListener();
		var pluginManager = plugin.getServer().getPluginManager();

		pluginManager.registerEvents(new InventoryBuilderDragListener(builderListener), plugin);
		pluginManager.registerEvents(new InventoryBuilderCloseListener(plugin, builderListener), plugin);
		pluginManager.registerEvents(new InventoryBuilderClickListener(builderListener), plugin);
	}

	/**
	 * Opens a {@link PersonalInventory} for a given {@link Player}.
	 * @param player The player to whom the inventory should be opened
	 * @param inventoryBuilder A valid instance to an {@link InventoryBuilder}
	 * @param onlyBuildIfNew Whether the inventory should be rebuilt
	 */

	public void openInventory(Player player, InventoryBuilder inventoryBuilder, boolean onlyBuildIfNew) {
		if (!onlyBuildIfNew || Objects.isNull(inventoryBuilder.getInventory())) {
			inventoryBuilder.buildInventory();
		}

		player.openInventory(inventoryBuilder.getInventory());
	}

	/**
	 * Opens a {@link PersonalInventory} for a given {@link Player}.
	 * @param player The player to whom the inventory should be opened
	 * @param personalInventory A instance to an {@link PersonalInventory}
	 */

	public void openPersonalInventory(Player player, PersonalInventory personalInventory) {
		openInventory(player, personalInventory, false);
	}
}