package de.icevizion.aves.inventory;

import com.google.common.collect.Maps;
import net.titan.spigot.player.CloudPlayer;
import net.titan.spigot.plugin.Service;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * This class contains some methods to open the different inventories available in the system.
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.0.0
 * @since 1.0.6
 */

public class InventoryService implements Service {

	private final Map<Class<? extends TranslatedInventory>, TranslatedCache> translatedInventories;

	public InventoryService() {
		translatedInventories = Maps.newHashMap();
	}

	/**
	 * Get a inventory from the underlying cache.
	 * @param inventoryClass The class from the inventory
	 * @return Returns all inventory that matches with the given class
	 */

	public Map<Locale, TranslatedInventory> getCachedInventories(
			Class<? extends TranslatedInventory> inventoryClass) {
		var translatedCache = translatedInventories.computeIfAbsent(inventoryClass,
				function -> new TranslatedCache());
		return translatedCache.getCachedInventories();
	}

	/**
	 * Opens a {@link PersonalInventory} for a given {@link CloudPlayer}.
	 * @param cloudPlayer The player to whom the inventory should be opened
	 * @param inventoryBuilder A valid instance to an {@link InventoryBuilder}
	 * @param onlyBuildIfNew Whether the inventory should be rebuilt
	 */

	public void openInventory(CloudPlayer cloudPlayer, InventoryBuilder inventoryBuilder,
	                           boolean onlyBuildIfNew) {
		var player = cloudPlayer.getPlayer();
		if(!onlyBuildIfNew || Objects.isNull(inventoryBuilder.getInventory())) {
			inventoryBuilder.buildInventory();
		}

		player.openInventory(inventoryBuilder.getInventory());
	}

	/**
	 * Opens a {@link PersonalInventory} for a given {@link CloudPlayer}.
	 * @param cloudPlayer The player to whom the inventory should be opened
	 * @param personalInventory A instance to an {@link PersonalInventory}
	 */

	public void openPersonalInventory(CloudPlayer cloudPlayer, PersonalInventory personalInventory) {
		openInventory(cloudPlayer, personalInventory, false);
	}


	/**
	 * Opens an inventory that extends {@link TranslatedInventory} for a given {@link CloudPlayer}.
	 * @param cloudPlayer The player to whom the inventory should be opened
	 * @param inventoryClass A class who extends {@link TranslatedInventory}
	 * @param ifAbsent The function what happens when the player has no registered inventory
	 */

	public void openTranslatedInventory(CloudPlayer cloudPlayer,
	                                    Class<? extends TranslatedInventory> inventoryClass,
	                                    Function<Locale, TranslatedInventory> ifAbsent) {
		TranslatedCache cache = translatedInventories.computeIfAbsent(inventoryClass,
				function -> new TranslatedCache());
		TranslatedInventory translatedInventory = cache.getInventory(cloudPlayer.getLocale(),
				ifAbsent);
		openInventory(cloudPlayer, translatedInventory, true);
	}
}
