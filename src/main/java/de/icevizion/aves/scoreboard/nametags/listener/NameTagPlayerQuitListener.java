package de.icevizion.aves.scoreboard.nametags.listener;

import de.icevizion.aves.scoreboard.ScoreboardBuilder;
import de.icevizion.aves.scoreboard.nametags.NameTagService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class NameTagPlayerQuitListener implements Listener {

	private final NameTagService nameTagService;

	public NameTagPlayerQuitListener(NameTagService nameTagService) {
		this.nameTagService = nameTagService;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		var cloudPlayer = nameTagService.getPlayer(event.getPlayer());
		nameTagService.removePlayer(cloudPlayer);
		ScoreboardBuilder.of(null, cloudPlayer).delete();
	}
}
