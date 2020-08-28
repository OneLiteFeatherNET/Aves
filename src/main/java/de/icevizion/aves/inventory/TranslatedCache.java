package de.icevizion.aves.inventory;

import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class TranslatedCache {

	private final Map<Locale, TranslatedInventory> cachedInventories;

	protected TranslatedCache() {
		cachedInventories = Maps.newHashMap();
	}

	public Map<Locale, TranslatedInventory> getCachedInventories() {
		return cachedInventories;
	}

	public TranslatedInventory getInventory(Locale locale,
	                                        Function<Locale, TranslatedInventory> function) {
		return cachedInventories.computeIfAbsent(locale, function);
	}
}
