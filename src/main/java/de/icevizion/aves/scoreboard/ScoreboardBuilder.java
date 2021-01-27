package de.icevizion.aves.scoreboard;

import com.google.common.collect.Maps;
import net.titan.cloudcore.i18n.Translator;
import net.titan.cloudcore.task.Task;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ScoreboardBuilder extends ScoreboardImpl {

	private static final Map<UUID, ScoreboardImpl> cachedScoreboards = Maps.newHashMap();

	private Translator translator;

	/**
	 * Instantiates a new Scoreboard builder.
	 *
	 * @param scoreboard  the scoreboard
	 * @param cloudPlayer the cloud player
	 */
	protected ScoreboardBuilder(Scoreboard scoreboard, CloudPlayer cloudPlayer) {
		super(scoreboard, cloudPlayer);
		cachedScoreboards.putIfAbsent(cloudPlayer.getUniqueId(), this);
	}

	/**
	 * Gets scoreboard builder by player and creates one if it isn't existing.
	 *
	 * @param translator  the translator
	 * @param cloudPlayer the cloud player
	 * @return the scoreboard builder
	 */
	public static ScoreboardBuilder create(Translator translator, CloudPlayer cloudPlayer) {
		ScoreboardBuilder scoreboard = (ScoreboardBuilder) cachedScoreboards.get(cloudPlayer.getUniqueId());
		if (Objects.isNull(scoreboard)) {
			scoreboard = new ScoreboardBuilder(Bukkit.getScoreboardManager().getNewScoreboard(), cloudPlayer);
		}

		scoreboard.setTranslator(translator);
		return scoreboard;
	}

	/**
	 * Gets scoreboard builder by player and base name of the translator.
	 *
	 * @param translator  the translator
	 * @param cloudPlayer the cloud player
	 * @return the scoreboard builder
	 * @throws IllegalArgumentException if no scoreboard builder is registered to the given player
	 */
	public static ScoreboardBuilder of(Translator translator, CloudPlayer cloudPlayer) {
		var scoreboard = cachedScoreboards.get(cloudPlayer.getUniqueId());

		if (Objects.isNull(scoreboard)) {
			throw new IllegalArgumentException("No scoreboard is registered for that player");
		}

		String baseName = Objects.isNull(translator) ? "null" : translator.getBaseName();

		return scoreboard.getCachedTranslations().computeIfAbsent(baseName,
				function -> {
					ScoreboardBuilder scoreboardBuilder = (ScoreboardBuilder) scoreboard;
					scoreboardBuilder.setTranslator(translator);
					return scoreboardBuilder;
				});
	}

	/**
	 * Add task to the scoreboard builder.
	 *
	 * @param task the task
	 * @return the scoreboard builder
	 */
	public ScoreboardBuilder addTask(Task task) {
		getTasks().add(task);
		return this;
	}

	/**
	 * Remove task from the scoreboard builder.
	 *
	 * @param task the task
	 * @return the scoreboard builder
	 */
	public ScoreboardBuilder removeTask(Task task) {
		task.cancel();
		getTasks().remove(task);
		return this;
	}

	/**
	 * Gets a objective by objective name and creates one if necessary.
	 *
	 * @param objectiveName the objective name
	 * @return the objective
	 */
	public ScoreboardObjective getObjective(String objectiveName) {
		return getObjectives().computeIfAbsent(objectiveName,
				function -> new ScoreboardObjective(this, objectiveName));
	}

	/**
	 * Gets the sidebar objective.
	 *
	 * @return the sidebar objective
	 */
	public ScoreboardObjective getSidebarObjective() {
		return getObjective("sidebar");
	}

	/**
	 * Gets a line by row and creates one if necessary.
	 *
	 * @param row the row
	 * @return the line
	 */
	public ScoreboardLine getLine(int row) {
		return getRows().computeIfAbsent(row, function -> new ScoreboardLine(this, row));
	}

	/**
	 * Remove a line from the sidebar
	 *
	 * @param row the row
	 * @return the scoreboard builder
	 */
	public ScoreboardBuilder removeLine(int row) {
		ScoreboardLine line = getRows().get(row);
		if(Objects.nonNull(line)) {
			line.delete();
		}

		return this;
	}

	/**
	 * Sets display name of the sidebar objective.
	 *
	 * @param key       the key
	 * @param arguments the arguments
	 * @return the display name
	 */
	public ScoreboardBuilder setDisplayName(String key, Object... arguments) {
		String displayName = translator.getString(getLocale(), key, arguments);
		getSidebarObjective().setDisplayName(displayName);
		return this;
	}

	/**
	 * Sets display slot of the sidebar objective.
	 *
	 * @param displaySlot the display slot
	 * @return the display slot
	 */
	public ScoreboardBuilder setDisplaySlot(DisplaySlot displaySlot) {
		getSidebarObjective().setDisplaySlot(displaySlot);
		return this;
	}

	/**
	 * Sets the display slot of a objective to null, which hides the objective.
	 *
	 * @param objectiveName the objective name
	 * @return the scoreboard builder
	 */
	public ScoreboardBuilder hideObjective(String objectiveName) {
		getObjective(objectiveName).setDisplaySlot(null);
		return this;
	}

	/**
	 * Hides the sidebar objective.
	 *
	 * @return the scoreboard builder
	 */
	public ScoreboardBuilder hideSidebar() {
		return hideObjective("sidebar");
	}

	/**
	 * Clears and deletes the scoreboard builder.
	 */
	public void delete() {
		unsetScoreboard();
		getObjectives().forEach((s, scoreboardObjective) -> scoreboardObjective.unregister());
		getObjectives().clear();
		getTasks().forEach(Task::cancel);
		cachedScoreboards.remove(getCloudPlayer().getUniqueId());
	}

	/**
	 * Gets translator.
	 *
	 * @return the translator
	 */
	protected Translator getTranslator() {
		return translator;
	}

	/**
	 * Gets locale.
	 *
	 * @return the locale
	 */
	protected Locale getLocale() {
		return Objects.isNull(getCloudPlayer()) ? translator.getDefaultLocale() : getCloudPlayer().getLocale();
	}

	/**
	 * Sets translator.
	 *
	 * @param translator the translator
	 */
	protected void setTranslator(Translator translator) {
		this.translator = translator;
	}
}
