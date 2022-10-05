package de.icevizion.aves.util;

import de.icevizion.aves.inventory.util.InventoryConstants;
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
        Objects.requireNonNull(player.getInstance(), "The instance from the player can't be null");
        dropItemStacks(player.getInstance(), player.getPosition(), player.getInventory().getItemStacks());
    }

    /**
     * Drops a certain amount of items to a given location.
     * @param content The items stored in an array
     */
    public static void dropItemStacks(@NotNull Instance instance, @NotNull Pos pos, @NotNull ItemStack @NotNull ... content) {
        if (content.length == 0) {
            throw new IllegalArgumentException("The array can not be null or empty");
        } else {
            for (int i = 0; i < content.length; i++) {
                if (content[i] == null) continue;
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
     * Updates the hotbar items from a given {@link Player}.
     * The locale and shiftedSlot parameter can be null
     * @param player The {@link Player} who receives the new equipment
     * @param hotbarItems The hot bar items as array
     * @param locale The {@link Locale} for {@link TranslatedItem}
     * @param shiftedSlots An array with contains shifted layout only for the hotbar
     */
    public static void updateHotBar(@NotNull Player player, @NotNull IItem[] hotbarItems, @Nullable Locale locale, int... shiftedSlots) {
        if (hotbarItems.length > InventoryConstants.INVENTORY_WIDTH) {
            throw new IllegalArgumentException("The array length for the items is greater than " + InventoryConstants.INVENTORY_WIDTH);
        }
        if (shiftedSlots.length > hotbarItems.length) {
            throw new IllegalArgumentException("The length from shiftedSlots has not the same length with the underlying array");
        }
        if (placer == null) {
            placer = ItemPlacer.FALLBACK;
            PLAYER_LOGGER.info("Set `ItemPlacer Interface` to fallback implementation");
        }
        setItems(player, hotbarItems, locale, shiftedSlots);
    }

    /**
     * Updates the armor items from a given {@link Player}.
     * The locale and shiftedSlot parameter can be null
     * @param player The {@link Player} who receives the new equipment
     * @param armorItems The array with the items for the armor area
     * @param locale The {@link Locale} for {@link TranslatedItem}
     */
    public static void updateArmorItems(@NotNull Player player, @NotNull IItem[] armorItems, @Nullable Locale locale) {
        if (placer == null) {
            placer = ItemPlacer.FALLBACK;
            PLAYER_LOGGER.info("Set `ItemPlacer Interface` to fallback implementation");
        }
        setItems(player, armorItems, locale);
    }

    /**
     * Applies a given array of {@link ItemStack}'s to a {@link Player}.
     * @param player the player who should get the items
     * @param items the array with the items
     * @param locale the locale if the case need some
     * @param shiftedSlots an array which contains shifted slots
     */
    private static void setItems(@NotNull Player player, @NotNull IItem[] items, Locale locale, int... shiftedSlots) {
        for (int i = 0; i < items.length; i++) {
            var wrappedItem = items[i];
            if (wrappedItem == null) continue;
            var slotID = shiftedSlots != null ? shiftedSlots[i] : i;
            placer.setItem(player, slotID, wrappedItem, locale, true);
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