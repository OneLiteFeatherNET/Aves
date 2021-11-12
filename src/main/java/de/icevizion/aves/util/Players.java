package de.icevizion.aves.util;

import de.icevizion.aves.item.IItem;
import de.icevizion.aves.item.TranslatedItem;
import de.icevizion.aves.util.functional.ItemPlacer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Contains some methods to work with {@link Player} objects
 */
public class Players {

    public static Duration ITEM_DURATION = Duration.ofMillis(3);
    public static ItemPlacer ITEM_PLACER;

    /**
     * Drops the complete inventory content from a player to a specific location.
     * @param player The player from which the inventory should be dropped
     */

    public static void dropPlayerInventory(@NotNull Player player) {
        Objects.requireNonNull(player.getInstance(), "The instance from the player can not be null");
        dropItemStacks(player.getInstance(), player.getPosition(), player.getInventory().getItemStacks());
    }

    /**
     * Drops a certain amount of items to a given location.
     * @param content The items stored in an array
     */

    public static void dropItemStacks(@NotNull Instance instance, @NotNull Pos pos, ItemStack... content) {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("The array can not be null or empty");
        } else {
            for (int i = 0; i < content.length; i++) {
                ItemEntity entity = new ItemEntity(content[i]);
                entity.setMergeable(true);
                entity.setPickupDelay(ITEM_DURATION);
                entity.setInstance(instance, pos.withY(y -> y + 1.5));
                entity.setVelocity(pos.direction().mul(6));
                entity.spawn();
            }
        }
    }

    /**
     * Choose a random player from all players who are currently online.
     * @return a random player
     */

    public static Optional<Player> getRandomPlayer() {
        var players = MinecraftServer.getConnectionManager().getOnlinePlayers();

        if (players.isEmpty()) {
            return Optional.empty();
        }

        return players.stream().collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected);
            return collected.stream();
        })).findAny();
    }

    /**
     * Updates the equipment from a {@link Player}.
     * The locale and shiftedSlot parameter can be null
     * @param player The {@link Player} who receives the new equipment
     * @param armorItems The armor items as array
     * @param hotBarItems The hot bar items as array
     * @param shiftedSlots An array with contains shifted layout only for the hotbar
     */

    public static void updateEquipment(@NotNull Player player,  @NotNull IItem[] armorItems,
                                       @NotNull IItem[] hotBarItems, int... shiftedSlots) {
        updateEquipment(player, armorItems, hotBarItems, null, shiftedSlots);
    }

    /**
     * Updates the equipment from a {@link Player}.
     * The locale and shiftedSlot parameter can be null
     * @param player The {@link Player} who receives the new equipment
     * @param armorItems The armor items as array
     * @param hotBarItems The hot bar items as array
     * @param locale The {@link Locale} for {@link TranslatedItem}
     * @param shiftedSlots An array with contains shifted layout only for the hotbar
     */

    public static void updateEquipment(@NotNull Player player, @NotNull IItem[] armorItems,
                                       @NotNull IItem[] hotBarItems, @Nullable Locale locale, int... shiftedSlots) {
        if (shiftedSlots != null && shiftedSlots.length != hotBarItems.length) {
            throw new IllegalArgumentException("The length from shiftedSlots has not the same length with the underlying array");
        }

        if (ITEM_PLACER == null) {
            ITEM_PLACER = ItemPlacer.DEFAULT;
            System.out.println("Set `ItemPlacer Interface` to default implementation");
        }

        player.getInventory().clear();

        if (armorItems != null) {
            for (int i = 0; i < armorItems.length; i++) {
                if (armorItems[i] == null) continue;
                if (armorItems[i] instanceof TranslatedItem && locale != null) {
                    ITEM_PLACER.setItem(player, i, armorItems[i].get(locale), true);
                    return;
                } else {
                    ITEM_PLACER.setItem(player, i, armorItems[i].get(), true);
                }
            }
        }

        if (hotBarItems != null) {
            for (int i = 0; i < hotBarItems.length; i++) {
                if (hotBarItems[i] == null) continue;
                //Shift slots according to shiftedSlots array
                int slot = shiftedSlots == null ? i : shiftedSlots[i];

                if (hotBarItems[i] instanceof TranslatedItem && locale != null) {
                    ITEM_PLACER.setItem(player, slot, hotBarItems[i].get(locale));
                } else {
                    ITEM_PLACER.setItem(player, i, hotBarItems[i].get());
                }
            }
        }
    }

    /**
     * Get a random player from a given list.
     * @param players A list which contains some player objects
     * @return a random player
     */

    public static Optional<Player> getRandomPlayer(@NotNull List<Player> players) {
        if (players.isEmpty()) return Optional.empty();
        return Optional.of(players.get(ThreadLocalRandom.current().nextInt(players.size())));
    }
}