package de.icevizion.aves.scoreboard.nametags;

import com.google.common.collect.Maps;
import net.titan.spigot.Cloud;
import net.titan.spigot.player.CloudPlayer;
import net.titan.spigot.plugin.Service;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class NameTagService implements Service {

	private static final Map<UUID, NameTagScoreboard> nameTagScoreboards = Maps.newHashMap();

	private final Cloud cloud;

	private boolean activated;

	public NameTagService() {
		this.cloud = Cloud.getInstance();
	}

	public Cloud getCloud() {
		return cloud;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public static NameTagScoreboard of(CloudPlayer cloudPlayer) {
		var tagBoard = nameTagScoreboards.get(cloudPlayer.getUniqueId());

		if (tagBoard == null) {
			throw new IllegalArgumentException("No NameTag has been registered for this player");
		}

		return tagBoard;
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
		cloud.getCurrentOnlinePlayers().forEach(this::loadPlayer);
	}

	public void removePlayer(CloudPlayer cloudPlayer) {
		if(!nameTagScoreboards.containsKey(cloudPlayer.getUniqueId())) {
			return;
		}

		NameTagScoreboard playerNameTag = nameTagScoreboards.remove(cloudPlayer.getUniqueId());
		playerNameTag.reset();
		nameTagScoreboards.values().forEach(
				nameTagScoreboard -> nameTagScoreboard.removePlayer(cloudPlayer));
	}

	public void removeOnlinePlayers() {
		cloud.getCurrentOnlinePlayers().forEach(cloudPlayer -> {
			nameTagScoreboards.get(cloudPlayer.getUniqueId()).reset();
		});
	}

	public void addPlayerTeam(CloudPlayer cloudPlayer) {
		nameTagScoreboards.values().forEach(
				nameTagScoreboard -> nameTagScoreboard.addPlayer(cloudPlayer));
	}

	public void loadOnlinePlayers(NameTagScoreboard nameTagScoreboard) {
		cloud.getCurrentOnlinePlayers().forEach(nameTagScoreboard::addPlayer);
	}
}
