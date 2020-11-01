package de.icevizion.aves.util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Contains some methods to work with {@link Player} objects
 */
public class PlayerUtil {

    /**
     * Drops the complete inventory content from a player to a specific location.
     * @param player The player from which the inventory should be dropped
     */

    public static void dropInventory(Player player) {
        Objects.requireNonNull(player, "The given player can not be null");

        PlayerInventory playerInventory = player.getInventory();

        if (playerInventory.getContents().length != 0) {
            dropInventory(player.getLocation(), playerInventory.getContents());
        }

        if (playerInventory.getArmorContents().length != 0) {
            dropInventory(player.getLocation(), playerInventory.getArmorContents());
        }

    }

    /**
     * Drops a certain amount of items to a given location.
     * @param location The location where the items should be dropped.
     * @param content The items stored in a array
     */

    public static void dropInventory(Location location, ItemStack[] content) {
        if (content == null) {
            throw new IllegalArgumentException("The array can not be null");
        } else {
            for (int i = 0; i < content.length; i++) {
                location.getWorld().dropItemNaturally(location, content[i]);
            }
        }
    }

    /**
     * Drops a certain amount of items to a given location.
     * @param location The location where the items should be dropped.
     * @param content The items stored in a list
     */

    public static void dropInventory(Location location, List<ItemStack> content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("The list can not be null or empty");
        } else {
            for (int i = 0; i < content.size(); i++) {
                location.getWorld().dropItem(location, content.get(i));
            }
        }
    }

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