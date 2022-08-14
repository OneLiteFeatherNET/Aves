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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Players {

    private static final Logger PLAYER_LOGGER = LoggerFactory.getLogger(Players.class);

    private static Duration itemDuration = Duration.ofMillis(3);
    private static ItemPlacer placer;

    private Players() {}

    /**
     * Set a new {@link Duration} for the item drop functionality.
     * @param duration the duration to set
     */
    public static void setItemDuration(@NotNull Duration duration) {
        itemDuration = duration;
    }

    /**
     * Set a new instance from a {@link ItemPlacer}.
     * @param itemPlacer the new instance to set
     */
    public static void setItemPlacer(@NotNull ItemPlacer itemPlacer) {
        placer = itemPlacer;
    }

    /**
     * Checks if a player has an instance or not
     * When the instance is null then the method will throw an exception
     * @param player The player to check
     */
    public static void hasInstance(@NotNull Player player) {
        if (player.getInstance() == null) {
            throw new IllegalArgumentException("The instance from a player can not be null");
        }
    }

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
                entity.setPickupDelay(itemDuration);
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

        if (players.size() == 1) {
            return players.stream().findAny();
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
    public static void updateEquipment(@NotNull Player player,
                                       @NotNull IItem[] armorItems,
                                       @NotNull IItem[] hotBarItems,
                                       int... shiftedSlots) {
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
    @SuppressWarnings("java:S3776")
    public static void updateEquipment(@NotNull Player player,
                                       @NotNull IItem[] armorItems,
                                       @NotNull IItem[] hotBarItems,
                                       @Nullable Locale locale,
                                       int... shiftedSlots) {
        if (shiftedSlots != null && shiftedSlots.length != hotBarItems.length) {
            throw new IllegalArgumentException("The length from shiftedSlots has not the same length with the underlying array");
        }

        if (placer == null) {
            placer = ItemPlacer.FALLBACK;
            PLAYER_LOGGER.info("Set `ItemPlacer Interface` to fallback implementation");
        }

        player.getInventory().clear();

        if (armorItems != null) {
            for (int i = 0; i < armorItems.length; i++) {
                if (armorItems[i] == null) continue;
                if (armorItems[i] instanceof TranslatedItem item && locale != null) {
                    placer.setItem(player, i, item.get(locale), true);
                    return;
                } else {
                    placer.setItem(player, i, armorItems[i].get(), true);
                }
            }
        }

        if (hotBarItems != null) {
            for (int i = 0; i < hotBarItems.length; i++) {
                if (hotBarItems[i] == null) continue;
                //Shift slots according to shiftedSlots array
                int slot = shiftedSlots == null ? i : shiftedSlots[i];

                if (hotBarItems[i] instanceof TranslatedItem item && locale != null) {
                    placer.setItem(player, slot, item.get(locale));
                } else {
                    placer.setItem(player, i, hotBarItems[i].get());
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