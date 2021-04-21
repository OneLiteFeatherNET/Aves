package de.icevizion.aves.scoreboard;

import net.titan.cloudcore.task.Task;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ScoreboardImpl {

	private final Scoreboard scoreboard;
	private final CloudPlayer cloudPlayer;

	//For what?
	private final Set<Task> tasks;

	private final Map<Integer, ScoreboardLine> rows;
	private final Map<String, ScoreboardObjective> objectives;
	private final Map<String, ScoreboardBuilder> cachedTranslations;

	/**
	 * Instantiates a new Scoreboard.
	 *
	 * @param scoreboard  the scoreboard
	 * @param cloudPlayer the cloud player
	 */
	protected ScoreboardImpl(Scoreboard scoreboard, CloudPlayer cloudPlayer) {
		this.scoreboard = scoreboard;
		this.cloudPlayer = cloudPlayer;

		tasks = new HashSet<>();
		rows = new HashMap<>();
		objectives = new HashMap<>();
		cachedTranslations = new HashMap<>();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ScoreboardImpl that = (ScoreboardImpl) o;
		return cloudPlayer.getUniqueId().equals(that.cloudPlayer.getUniqueId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(cloudPlayer.getUniqueId());
	}

	/**
	 * Gets bukkit scoreboard.
	 *
	 * @return the bukkit scoreboard
	 */
	public Scoreboard getBukkitScoreboard() {
		return scoreboard;
	}

	/**
	 * Gets team by the team name and creates one by the given name if the team isn't existing yet.
	 *
	 * @param teamName the team name
	 * @return the team
	 */
	public Team getTeam(Object teamName) {
		Optional<Team> optionalTeam = getOptionalTeam(teamName);
		return optionalTeam.orElseGet(() -> scoreboard.registerNewTeam(teamName.toString()));
	}

	/**
	 * Gets optional team by the team name. Returns an empty optional if the team doesn't exist.
	 *
	 * @param teamName the team name
	 * @return the optional team
	 */
	public Optional<Team> getOptionalTeam(Object teamName) {
		return Optional.ofNullable(scoreboard.getTeam(teamName.toString()));
	}

	/**
	 * Checks if a team exists by the team name.
	 *
	 * @param teamName the team name
	 * @return the boolean
	 */
	public boolean teamExists(String teamName) {
		return scoreboard.getTeam(teamName) != null;
	}

	/**
	 * Sets the scoreboard.
	 */
	public void setScoreboard() {
		cloudPlayer.getPlayer().setScoreboard(scoreboard);
	}

	/**
	 * Unset current scoreboard by using the servers main scoreboard.
	 */
	public void unsetScoreboard() {
		cloudPlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	/**
	 * Gets the cloud player.
	 *
	 * @return the cloud player
	 */
	protected CloudPlayer getCloudPlayer() {
		return cloudPlayer;
	}

	/**
	 * Gets tasks.
	 *
	 * @return the tasks
	 */
	protected Set<Task> getTasks() {
		return tasks;
	}

	/**
	 * Gets rows.
	 *
	 * @return the rows
	 */
	protected Map<Integer, ScoreboardLine> getRows() {
		return rows;
	}

	/**
	 * Gets objectives.
	 *
	 * @return the objectives
	 */
	protected Map<String, ScoreboardObjective> getObjectives() {
		return objectives;
	}

	/**
	 * Gets cached translations.
	 *
	 * @return the cached translations
	 */
	protected Map<String, ScoreboardBuilder> getCachedTranslations() {
		return cachedTranslations;
	}
}

