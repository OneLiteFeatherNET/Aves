package de.icevizion.aves.scoreboard.nametags;

import net.titan.cloudcore.i18n.Translator;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.scoreboard.Team;

import java.util.Locale;
import java.util.Random;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class NameTagTeam {

	private static final Random RANDOM = new Random();

	private final NameTagScoreboard nameTag;
	private final Team team;

	protected NameTagTeam(NameTagScoreboard nameTag, Team team) {
		this.nameTag = nameTag;
		this.team = team;
	}

	protected NameTagTeam(NameTagScoreboard nameTag, CloudPlayer cloudPlayer) {
		this.nameTag = nameTag;
		String teamName = cloudPlayer.getRank().getSortId() + cloudPlayer.getDisplayName();
		if (teamName.length() > 15) {
			teamName = teamName.substring(0, 15);
		}

		team = nameTag.getScoreboard().registerNewTeam(teamName + RANDOM.nextInt(10));
		team.addEntry(cloudPlayer.getDisplayName());
	}

	public Team getTeam() {
		return team;
	}

	/**
	 * Go back to the name tag scoreboard.
	 *
	 * @return the name tag scoreboard
	 */
	public NameTagScoreboard apply() {
		return nameTag;
	}

	/**
	 * Sets a non-translated prefix for the team.
	 *
	 * @param prefix the prefix
	 * @return the name tag team
	 */
	public NameTagTeam setPrefix(String prefix) {
		team.setPrefix(prefix);
		return this;
	}

	/**
	 * Sets a translated prefix
	 *
	 * @param translator the translator
	 * @param locale     the locale
	 * @param key        the key
	 * @param arguments  the arguments
	 * @return the name tag team
	 */
	public NameTagTeam setPrefix(Translator translator, Locale locale, String key,
	                             Object... arguments) {
		return setPrefix(translator.getString(locale, key, arguments));
	}

	public NameTagTeam setPrefix(Translator translator, CloudPlayer cloudPlayer, String key,
	                             Object... arguments) {
		return setPrefix(translator, cloudPlayer.getLocale(), key, arguments);
	}

	/**
	 * Reset the prefix.
	 *
	 * @return the name tag scoreboard
	 */
	public NameTagScoreboard resetPrefix() {
		team.setPrefix("");
		return nameTag;
	}

	/**
	 * Sets a non-translated suffix for the team.
	 *
	 * @param suffix the prefix
	 * @return the name tag team
	 */
	public NameTagTeam setSuffix(String suffix) {
		team.setSuffix(suffix);
		return this;
	}

	public NameTagTeam setSuffix(Translator translator, Locale locale, String key,
	                             Object... arguments) {
		return setSuffix(translator.getString(locale, key, arguments));
	}

	public NameTagTeam setSuffix(Translator translator, CloudPlayer cloudPlayer, String key,
	                             Object... arguments) {
		return setSuffix(translator, cloudPlayer.getLocale(), key, arguments);
	}

	/**
	 * Resets the suffix.
	 *
	 * @return the name tag scoreboard
	 */
	public NameTagScoreboard resetSuffix() {
		team.setSuffix("");
		return nameTag;
	}
}