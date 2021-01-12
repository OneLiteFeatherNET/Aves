package de.icevizion.aves.inventory.listener;

import de.icevizion.aves.inventory.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryBuilderListener {

	protected Optional<InventoryBuilder.Holder> getInventoryHolder(InventoryEvent event,
	                                                               Player player) {
		Inventory clickedInventory = event.getInventory();
		if (Objects.isNull(clickedInventory.getHolder())
				|| !(clickedInventory.getHolder() instanceof InventoryBuilder.Holder)) {
			return Optional.empty();
		}

		InventoryBuilder.Holder holder = (InventoryBuilder.Holder) clickedInventory.getHolder();

		return !holder.getInventory().getViewers().contains(player) ? Optional.empty() : Optional.of(holder);
	}
}
