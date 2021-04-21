package de.icevizion.aves.scoreboard.nametags;

import de.icevizion.aves.scoreboard.ScoreboardBuilder;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class NameTagScoreboard {

	private final ScoreboardBuilder scoreboardBuilder;
	private final NameTagService nameTagService;
	private final CloudPlayer cloudPlayer;
	private final Scoreboard scoreboard;
	private final Map<String, NameTagTeam> teams;

	private NameTagScoreboard(NameTagService nameTagService, CloudPlayer cloudPlayer) {
		this.nameTagService = nameTagService;
		this.cloudPlayer = cloudPlayer;

		scoreboardBuilder = ScoreboardBuilder.create(null, cloudPlayer);
		scoreboard = scoreboardBuilder.getBukkitScoreboard();

		teams = new HashMap<>();
	}

	/**
	 * Instantiates a new name tag scoreboard.
	 *
	 * @param nameTagService the name tags instance
	 * @param cloudPlayer    the cloud player
	 * @return the name tag scoreboard
	 */
	public static NameTagScoreboard of(NameTagService nameTagService, CloudPlayer cloudPlayer) {
		return new NameTagScoreboard(nameTagService, cloudPlayer);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NameTagScoreboard that = (NameTagScoreboard) o;
		return cloudPlayer.getUniqueId().equals(that.cloudPlayer.getUniqueId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(cloudPlayer.getUniqueId());
	}

	/**
	 * Gets the cloud player.
	 *
	 * @return the cloud player
	 */
	public CloudPlayer getCloudPlayer() {
		return cloudPlayer;
	}

	/**
	 * Gets the scoreboard.
	 *
	 * @return the scoreboard
	 */
	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	/**
	 * Gets name tag team by player.
	 *
	 * @param cloudPlayer the cloud player
	 * @return the name tag team
	 */

	//TODO: Improve...

	public NameTagTeam getTeam(CloudPlayer cloudPlayer) {
		return teams.computeIfAbsent(cloudPlayer.getDisplayName(), function -> {
			Team entryTeam = scoreboard.getEntryTeam(cloudPlayer.getDisplayName());
			if (Objects.nonNull(entryTeam) && !entryTeam.getEntries().contains("§0null")) {
				return new NameTagTeam(this, entryTeam);
			}

			return new NameTagTeam(this, cloudPlayer);
		});
	}

	/**
	 * Adds player.
	 *
	 * @param cloudPlayer the cloud player
	 */
	public void addPlayer(CloudPlayer cloudPlayer) {
		NameTagTeam team = getTeam(cloudPlayer);
		team.setPrefix(cloudPlayer.getRank().getPrefix());
		scoreboard.resetScores(cloudPlayer.getDisplayName());
	}

	/**
	 * Remove player from scoreboard.
	 *
	 * @param cloudPlayer the cloud player
	 */
	public void removePlayer(CloudPlayer cloudPlayer) {
		Team team = getTeam(cloudPlayer).getTeam();
		team.removeEntry(cloudPlayer.getDisplayName());
		team.addEntry("§0null");
		team.unregister();
		teams.remove(cloudPlayer.getDisplayName());
	}

	/**
	 * Load name tag scoreboard.
	 */
	public void load() {
		nameTagService.addPlayerTeam(cloudPlayer);
		nameTagService.loadOnlinePlayers(this);
		scoreboardBuilder.setScoreboard();
	}

	public void reset() {
		if (teams.isEmpty()) return;
		teams.forEach((name, nameTagTeam) -> {
			nameTagTeam.getTeam().removeEntry(name);
			nameTagTeam.getTeam().addEntry("§0null");
			nameTagTeam.getTeam().unregister();
		});
		teams.clear();
	}
}
