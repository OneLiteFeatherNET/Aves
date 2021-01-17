package de.icevizion.aves.inventory;

import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * The class acts as a cache for all inventories created with the system.
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.0.0
 * @since 1.0.6
 */

public class TranslatedCache {

	private final Map<Locale, TranslatedInventory> cachedInventories;

	protected TranslatedCache() {
		cachedInventories = Maps.newHashMap();
	}

	/**
	 * Returns the map where all the inventories are stored.
	 * @return the underlying map
	 */

	public Map<Locale, TranslatedInventory> getCachedInventories() {
		return cachedInventories;
	}

	/**
	 * Get an inventory from the underlying cache.
	 * @param locale The language that acts as a key
	 * @param function The function that includes the logic what happens when there is no inventory for the language
	 * @return The fetched {@link TranslatedInventory}
	 */

	public TranslatedInventory getInventory(Locale locale,
	                                        Function<Locale, TranslatedInventory> function) {
		return cachedInventories.computeIfAbsent(locale, function);
	}
}
