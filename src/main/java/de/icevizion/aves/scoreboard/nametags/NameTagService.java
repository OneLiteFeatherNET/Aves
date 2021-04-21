package de.icevizion.aves.scoreboard.nametags;

import de.icevizion.aves.scoreboard.nametags.listener.NameTagPlayerJoinListener;
import de.icevizion.aves.scoreboard.nametags.listener.NameTagPlayerQuitListener;
import net.titan.spigot.Cloud;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class NameTagService {

	private static final Map<UUID, NameTagScoreboard> nameTagScoreboards = new HashMap<>();

	private boolean activated;

	public NameTagService(Plugin plugin, boolean activated) {
		this.activated = activated;
		plugin.getServer().getPluginManager().registerEvents(new NameTagPlayerJoinListener(this), plugin);
		plugin.getServer().getPluginManager().registerEvents(new NameTagPlayerQuitListener(this), plugin);
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public boolean isActivated() {
		return activated;
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
		var nameTagScoreboard = nameTagScoreboards.computeIfAbsent(
				cloudPlayer.getUniqueId(), function -> NameTagScoreboard.of(this, cloudPlayer));
		nameTagScoreboard.load();
	}

	public void loadOnlinePlayers() {
		Cloud.getInstance().getCurrentOnlinePlayers().forEach(this::loadPlayer);
	}

	public void removePlayer(CloudPlayer cloudPlayer) {
		var playerTag = nameTagScoreboards.remove(cloudPlayer.getUniqueId());
		if (playerTag == null) return;
		playerTag.reset();
		for (NameTagScoreboard scoreboard : nameTagScoreboards.values()) {
			scoreboard.removePlayer(cloudPlayer);
		}
	}

	public void removeOnlinePlayers() {
		for (CloudPlayer cloudPlayer : Cloud.getInstance().getCurrentOnlinePlayers()) {
			nameTagScoreboards.get(cloudPlayer.getUniqueId()).reset();
		}
	}

	public void addPlayerTeam(CloudPlayer cloudPlayer) {
		for (NameTagScoreboard scoreboard : nameTagScoreboards.values()) {
			scoreboard.addPlayer(cloudPlayer);
		}
	}

	public void loadOnlinePlayers(NameTagScoreboard nameTagScoreboard) {
		for (CloudPlayer cloudPlayer : Cloud.getInstance().getCurrentOnlinePlayers()) {
			nameTagScoreboard.addPlayer(cloudPlayer);
		}
	}

	public CloudPlayer getPlayer(Player player) {
		return Cloud.getInstance().getPlayer(player);
	}
}
