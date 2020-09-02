package de.icevizion.aves.scoreboard.nametags.listener;

import de.icevizion.aves.scoreboard.nametags.NameTagService;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class NameTagPlayerJoinListener implements Listener {

	private final NameTagService nameTagService;

	public NameTagPlayerJoinListener(NameTagService nameTagService) {
		this.nameTagService = nameTagService;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		CloudPlayer cloudPlayer = nameTagService.getCloudService().getPlayer(event.getPlayer());
		nameTagService.loadPlayer(cloudPlayer);
	}
}
