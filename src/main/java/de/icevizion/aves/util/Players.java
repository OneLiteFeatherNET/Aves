package de.icevizion.aves.util;

import de.icevizion.aves.item.IItem;
import de.icevizion.aves.item.TranslatedItem;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Contains some methods to work with {@link Player} objects
 */
public class Players {

    /**
     * Drops the complete inventory content from a player to a specific location.
     * @param player The player from which the inventory should be dropped
     */

    public static void dropInventory(@NotNull Player player) {
        Objects.requireNonNull(player, "The given player can not be null");

        PlayerInventory playerInventory = player.getInventory();

        if (playerInventory.getItemStacks().length != 0) {
            dropInventory(player.getPosition(), playerInventory.getItemStacks());
        }
    }

    /**
     * Drops a certain amount of items to a given location.
     * @param location The location where the items should be dropped.
     * @param content The items stored in a array
     */

    public static void dropInventory(Pos location, ItemStack[] content) {
        if (content == null) {
            throw new IllegalArgumentException("The array can not be null");
        } else {
            for (int i = 0; i < content.length; i++) {
            }
        }
    }

    /**
     * Choose a random player from all players who are currently online.
     * @return a random player
     */

    public static Optional<Player> getRandomPlayer() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers().stream().collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected);
            return collected.stream();
        })).findAny();
    }

    public static void updateEquipment(Player player, IItem[] armorItems, IItem[] hotBarItems, int[] shiftedSlots) {
        if (shiftedSlots != null && shiftedSlots.length != hotBarItems.length) {
            throw new IllegalArgumentException("The length from shiftedSlots has not the same length with the underlying array");
        }

        player.getInventory().clear();
      //  var cloudPlayer = Cloud.getInstance().getPlayer(player);

        if (armorItems != null) {
            for (int i = 0; i < armorItems.length; i++) {
                if (armorItems[i] == null) continue;
                if (armorItems[i] instanceof TranslatedItem) {
                  //  setArmor(player, i, armorItems[i].get(cloudPlayer.getLocale()));
                    return;
                } else {
                   // setArmor(player, i, armorItems[i].get());
                }
            }
        }

        if (hotBarItems != null) {
            for (int i = 0; i < hotBarItems.length; i++) {
                if (hotBarItems[i] == null) continue;
                //Shift slots according to shiftedSlots array
                int slot = shiftedSlots == null ? i : shiftedSlots[i];

                if (hotBarItems[i] instanceof TranslatedItem) {
                  //  player.getInventory().setItem(slot, hotBarItems[i].get(cloudPlayer.getLocale()));
                } else {
                  //  player.getInventory().setItem(slot, hotBarItems[i].get());
                }
            }
        }
    }

    public static void setArmor(Player player, int index, ItemStack stack) {
        switch (index) {
            case 0:
                player.getInventory().setHelmet(stack);
                break;
            case 1:
                player.getInventory().setChestplate(stack);
                break;
            case 2:
                player.getInventory().setLeggings(stack);
                break;
            case 3:
                player.getInventory().setBoots(stack);
                break;
        }
    }

    /**
     * Get a random player from a given list.
     * @param players A list which contains some player objects
     * @return a random player
     */

    public static Optional<Player> getRandomPlayer(List<Player> players) {
        var random = new Random(players.size());
        return Optional.of(players.get(random.nextInt(players.size())));
    }
}