package de.icevizion.aves.scoreboard.nametags;

import com.google.common.collect.Maps;
import net.titan.spigot.CloudService;
import net.titan.spigot.player.CloudPlayer;
import net.titan.spigot.plugin.Service;
import org.bukkit.Server;

import java.text.MessageFormat;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class NameTagService implements Service {

	private static final Map<UUID, NameTagScoreboard> nameTagScoreboards = Maps.newHashMap();

	private final CloudService cloudService;

	public NameTagService(CloudService cloudService) {
		this.cloudService = cloudService;
	}

	public CloudService getCloudService() {
		return cloudService;
	}

	public static NameTagScoreboard of(CloudPlayer cloudPlayer) {
		if (!nameTagScoreboards.containsKey(cloudPlayer.getUniqueId())) {
			throw new IllegalArgumentException("No NameTag has been Registered for this player");
		}

		return nameTagScoreboards.get(cloudPlayer.getUniqueId());
	}

	public void forEach(Consumer<NameTagScoreboard> nameTagScoreboard) {
		nameTagScoreboards.values().forEach(nameTagScoreboard);
	}

	public void loadPlayer(CloudPlayer cloudPlayer) {
		NameTagScoreboard nameTagScoreboard = nameTagScoreboards.computeIfAbsent(
				cloudPlayer.getUniqueId(), function -> NameTagScoreboard.of(this, cloudPlayer));
		nameTagScoreboard.load();
	}

	public void loadOnlinePlayers() {
		cloudService.getOnlinePlayers().forEach(this::loadPlayer);
	}

	public void removePlayer(CloudPlayer cloudPlayer) {
		NameTagScoreboard playerNameTag = nameTagScoreboards.remove(cloudPlayer.getUniqueId());
		playerNameTag.reset();
		nameTagScoreboards.values().forEach(
				nameTagScoreboard -> nameTagScoreboard.removePlayer(cloudPlayer));
	}

	public void removeOnlinePlayers() {
		cloudService.getOnlinePlayers().forEach(cloudPlayer -> {
			NameTagScoreboard nameTagScoreboard = nameTagScoreboards.get(cloudPlayer.getUniqueId());
			nameTagScoreboard.reset();
		});
	}

	public void addPlayerTeam(CloudPlayer cloudPlayer) {
		nameTagScoreboards.values().forEach(
				nameTagScoreboard -> nameTagScoreboard.addPlayer(cloudPlayer));
	}

	public void loadOnlinePlayers(NameTagScoreboard nameTagScoreboard) {
		cloudService.getOnlinePlayers().forEach(nameTagScoreboard::addPlayer);
	}
}
