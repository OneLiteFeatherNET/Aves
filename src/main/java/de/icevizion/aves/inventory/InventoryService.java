package de.icevizion.aves.inventory;

import com.google.common.collect.Maps;
import net.titan.spigot.player.CloudPlayer;
import net.titan.spigot.plugin.Service;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryService implements Service {

	private final Map<Class<? extends TranslatedInventory>, TranslatedCache> translatedInventories;

	public InventoryService() {
		translatedInventories = Maps.newHashMap();
	}

	public Map<Locale, TranslatedInventory> getCachedInventories(
			Class<? extends TranslatedInventory> inventoryClass) {
		TranslatedCache translatedCache = translatedInventories.computeIfAbsent(inventoryClass,
				function -> new TranslatedCache());
		return translatedCache.getCachedInventories();
	}

	public void openInventory(CloudPlayer cloudPlayer, InventoryBuilder inventoryBuilder) {
		Player player = cloudPlayer.getPlayer();
		player.openInventory(inventoryBuilder.getInventory());
	}

	public void openPersonalInventory(CloudPlayer cloudPlayer, PersonalInventory personalInventory) {
		openInventory(cloudPlayer, personalInventory);
	}

	public void openTranslatedInventory(CloudPlayer cloudPlayer,
	                                    Class<? extends TranslatedInventory> inventoryClass,
	                                    Function<Locale, TranslatedInventory> ifAbsent) {
		TranslatedCache cache = translatedInventories.computeIfAbsent(inventoryClass,
				function -> new TranslatedCache());
		TranslatedInventory translatedInventory = cache.getInventory(cloudPlayer.getLocale(),
				ifAbsent);
		openInventory(cloudPlayer, translatedInventory);
	}
}
