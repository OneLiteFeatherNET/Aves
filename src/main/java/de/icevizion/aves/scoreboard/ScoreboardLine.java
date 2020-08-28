package de.icevizion.aves.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import java.util.Random;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ScoreboardLine {

	private static final Random RANDOM = new Random();

	private final ScoreboardBuilder scoreboardBuilder;
	private final Team team;
	private final String entry;
	private final int row;

	/**
	 * Instantiates a new Scoreboard line.
	 *
	 * @param scoreboardBuilder the scoreboard builder
	 * @param row               the row
	 */
	protected ScoreboardLine(ScoreboardBuilder scoreboardBuilder, int row) {
		this.scoreboardBuilder = scoreboardBuilder;
		this.row = row;

		team = scoreboardBuilder.getTeam(row);
		entry = generateRowEntry();
	}

	/**
	 * Calculates a translated value and sets it as prefix and suffix of the team.
	 *
	 * @param key       the resource bundle key
	 * @param arguments the arguments
	 * @return the scoreboard builder
	 */
	public ScoreboardBuilder setValue(String key, Object... arguments) {
		String value = scoreboardBuilder.getTranslator().getString(scoreboardBuilder.getLocale(), key,
				arguments);
		return setStaticValue(value);
	}

	/**
	 * Calculates a non-translated value and sets it as prefix and suffix of the team.
	 *
	 * @param value the value
	 * @return the scoreboard builder
	 */
	public ScoreboardBuilder setStaticValue(String value) {
		String[] teamStrings = calculateValueStrings(value);
		team.setPrefix(teamStrings[0]);
		team.setSuffix(teamStrings[1]);
		return apply();
	}

	/**
	 * Adds team to scoreboard if not already happened and goes back to the scoreboard builder.
	 *
	 * @return the scoreboard builder
	 */
	public ScoreboardBuilder apply() {
		if (!team.hasEntry(entry)) {
			team.addEntry(entry);
			scoreboardBuilder.getSidebarObjective().getScore(entry).setScore(row);
		}

		return scoreboardBuilder;
	}

	/**
	 * Sets a translated prefix for the team.
	 *
	 * @param key       the resource bundle key
	 * @param arguments the arguments
	 * @return the scoreboard line
	 */
	public ScoreboardLine setPrefix(String key, Object... arguments) {
		String prefix = scoreboardBuilder.getTranslator().getString(scoreboardBuilder.getLocale(), key,
				arguments);
		return setStaticPrefix(prefix);
	}

	/**
	 * Sets a non-translated prefix for the team.
	 *
	 * @param prefix the prefix
	 * @return the scoreboard line
	 */
	public ScoreboardLine setStaticPrefix(String prefix) {
		if (prefix.length() > 16) {
			prefix = prefix.substring(0, 16);
		}

		team.setPrefix(prefix);
		return this;
	}

	/**
	 * Sets a translated suffix for the team.
	 *
	 * @param key       the resource bundle key
	 * @param arguments the arguments
	 * @return the scoreboard line
	 */
	public ScoreboardLine setSuffix(String key, Object... arguments) {
		String suffix = scoreboardBuilder.getTranslator().getString(scoreboardBuilder.getLocale(), key,
				arguments);
		return setStaticSuffix(suffix);
	}

	/**
	 * Sets a non-translated suffix for the team.
	 *
	 * @param suffix the prefix
	 * @return the scoreboard line
	 */
	public ScoreboardLine setStaticSuffix(String suffix) {
		if (suffix.length() > 16) {
			suffix = suffix.substring(0, 16);
		}

		team.setSuffix(suffix);
		return this;
	}

	private String generateRowEntry() {
		String rowEntry = "§" + randomColorCode() + "§" + randomColorCode() + "§" + randomColorCode();
		if (scoreboardBuilder.teamExists(rowEntry)) {
			return generateRowEntry();
		}

		return rowEntry;
	}

	private String[] calculateValueStrings(String string) {
		if (string.length() <= 16) {
			return new String[]{string, ""};
		}

		String prefix = string.substring(0, 16);
		String suffix = string.substring(16);
		String lastColors = ChatColor.getLastColors(prefix);
		if (prefix.charAt(15) == '§') {
			prefix = prefix.substring(0, prefix.length() - 1);
			suffix = "§" + suffix;
		} else if (prefix.charAt(14) == '§') {
			prefix = prefix.substring(0, prefix.length() - lastColors.length());
			suffix = lastColors + suffix;
		}

		if (suffix.charAt(0) != '§') {
			suffix = lastColors + suffix;
		}

		if (prefix.length() + suffix.length() > 32) {
			suffix = suffix.substring(0, 16);
		}

		return new String[]{prefix, suffix};
	}

	private int randomColorCode() {
		return RANDOM.nextInt(10);
	}
}
