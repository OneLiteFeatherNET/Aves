package de.icevizion.aves.scoreboard;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Objects;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ScoreboardObjective {

	private final ScoreboardBuilder scoreboard;
	private final Objective objective;

	/**
	 * Instantiates a new Scoreboard objective.
	 *
	 * @param scoreboardBuilder the scoreboard builder
	 * @param objectiveName     the objective name
	 */
	protected ScoreboardObjective(ScoreboardBuilder scoreboardBuilder, String objectiveName) {
		this.scoreboard = scoreboardBuilder;

		Objective currentObjective = scoreboardBuilder.getBukkitScoreboard().getObjective(
				objectiveName);
		if (Objects.isNull(currentObjective)) {
			objective = scoreboardBuilder.getBukkitScoreboard().registerNewObjective(objectiveName,
					"dummy");
		} else {
			objective = currentObjective;
		}
	}

	/**
	 * Gets the Bukkit objective.
	 *
	 * @return the objective
	 */
	public Objective getObjective() {
		return objective;
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return objective.getDisplayName();
	}

	/**
	 * Gets the display slot.
	 *
	 * @return the display slot
	 */
	public DisplaySlot getDisplaySlot() {
		return objective.getDisplaySlot();
	}

	/**
	 * Gets score.
	 *
	 * @param entry the entry
	 * @return the score
	 */
	public Score getScore(String entry) {
		return objective.getScore(entry);
	}

	/**
	 * Go back to the scoreboard builder.
	 *
	 * @return the scoreboard builder
	 */
	public ScoreboardBuilder apply() {
		return scoreboard;
	}

	/**
	 * Sets display name of the objective.
	 *
	 * @param displayName the display name
	 * @return the display name
	 */
	public ScoreboardObjective setDisplayName(String displayName) {
		if (displayName.length() > 32) {
			displayName = displayName.substring(0, 32);
		}

		objective.setDisplayName(displayName);
		return this;
	}

	/**
	 * Sets display slot of the objective.
	 *
	 * @param displaySlot the display slot
	 * @return the display slot
	 */
	public ScoreboardObjective setDisplaySlot(DisplaySlot displaySlot) {
		objective.setDisplaySlot(displaySlot);
		return this;
	}

	/**
	 * Unregister the current objective.
	 */
	public void unregister() {
		objective.unregister();
	}
}
