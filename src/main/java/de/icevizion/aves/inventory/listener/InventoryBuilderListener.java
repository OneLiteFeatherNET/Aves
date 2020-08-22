package de.icevizion.aves.inventory.listener;

import de.icevizion.aves.inventory.InventoryBuilder;
import net.titan.spigot.CloudService;
import net.titan.spigotcore.TitanService;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class InventoryBuilderListener {

	private final CloudService cloudService;

	public InventoryBuilderListener(CloudService cloudService) {
		this.cloudService = cloudService;
	}

	protected CloudService getCloudService() {
		return cloudService;
	}

	protected Optional<InventoryBuilder.Holder> getInventoryHolder(InventoryEvent event,
	                                                               Player player) {
		Inventory clickedInventory = event.getInventory();
		if (Objects.isNull(clickedInventory.getHolder())
				|| !(clickedInventory.getHolder() instanceof InventoryBuilder.Holder)) {
			return Optional.empty();
		}

		InventoryBuilder.Holder holder = (InventoryBuilder.Holder) clickedInventory.getHolder();
		if (!holder.getInventory().getViewers().contains(player)) {
			return Optional.empty();
		}

		return Optional.of(holder);
	}
}
