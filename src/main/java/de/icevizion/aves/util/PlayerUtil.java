package de.icevizion.aves.util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.github.paperspigot.Title;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Contains some methods to work with {@link Player} objects
 */
public class PlayerUtil {

    /**
     * Choose a random player from all players who are currently online.
     * @return a random player
     */

    public static Optional<Player> getRandomPlayer() {
        return Bukkit.getOnlinePlayers().stream().collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected);
            return collected.stream();
        })).map(t -> ((Player) t)).findAny();
    }

    /**
     * Get a random player from a given list.
     * @param players A list which contains some player objects
     * @return a random player
     */

    public static Optional<Player> getRandomPlayer(List<Player> players) {
        return Optional.of(players.get(new Random(players.size()).nextInt(players.size())));
    }

    /**
     * Send a title to a given player.
     * @param player The player who should get the title
     * @param title The title of the title
     * @param subtitle The subtitle of the title
     * @param fadeIn The time how long the title fade in
     * @param stay The time how long the title stays
     * @param fadeOut The time how long the title fade out
     */

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(new Title(title, subtitle, fadeIn, stay, fadeOut));
    }

    /**
     * Send a title to a given player.
     * @param player The player who should get the title
     * @param builder Builder instance to build a title
     */

    public static void sendTitle(Player player, Title.Builder builder) {
        player.sendTitle(builder.build());
    }

    /**
     * Send tab and footer to a given player.
     * @param player The player who should get the information
     * @param header The header line
     * @param footer The footer line
     */

    public static void sendTab(Player player, String header, String footer) {
        player.setPlayerListHeaderFooter(new TextComponent(header), new TextComponent(footer));
    }

    /**
     * Send tab and footer to a given player.
     * @param player The player who should get the information
     * @param headers Multiple header lines
     * @param footers Multiple footer line
     */

    public static void sendTab(Player player, String[] headers, String[] footers) {
        BaseComponent[] headerComponents = new BaseComponent[headers.length];
        BaseComponent[] footerComponents = new BaseComponent[footers.length];

        for (int i = 0; i < headers.length; i++) {
            headerComponents[i] = new TextComponent(headers[i]);
        }

        for (int i = 0; i < footers.length; i++) {
            headerComponents[i] = new TextComponent(footers[i]);
        }

        player.setPlayerListHeaderFooter(headerComponents, footerComponents);
    }
}