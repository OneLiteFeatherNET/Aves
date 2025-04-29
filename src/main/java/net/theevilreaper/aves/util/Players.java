package net.theevilreaper.aves.util;

import net.theevilreaper.aves.inventory.util.InventoryConstants;
import net.theevilreaper.aves.item.IItem;
import net.theevilreaper.aves.item.TranslatedItem;
import net.theevilreaper.aves.util.functional.ItemPlacer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.SetCooldownPacket;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Contains some methods to work with {@link Player} objects
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public final class Players {

    private static final Logger PLAYER_LOGGER = LoggerFactory.getLogger(Players.class);
    private static Duration itemDuration = Duration.ofMillis(3);
    private static ItemPlacer placer;

    private Players() {
    }

    /**
     * Set a new {@link Duration} for the item drop functionality.
     *
     * @param duration the duration to set
     */
    public static void setItemDuration(@NotNull Duration duration) {
        itemDuration = duration;
    }

    /**
     * Set a new instance from a {@link ItemPlacer}.
     *
     * @param itemPlacer the new instance to set
     */
    public static void setItemPlacer(@NotNull ItemPlacer itemPlacer) {
        placer = itemPlacer;
    }

    /**
     * Send a title to a given player.
     *
     * @param player   the player who receives the title
     * @param title    the title message as {@link Component}
     * @param subTitle the subTitle message as {@link Component}
     * @param fadeIn   the time to fade in
     * @param stay     the time how long the title stays
     * @param fadeOut  the time to fade out
     */
    public static void showTitle(@NotNull Player player, @NotNull Component title, @NotNull Component subTitle, int fadeIn, int stay, int fadeOut) {
        player.showTitle(Title.title(title, subTitle, Title.Times.times(Ticks.duration(fadeIn), Ticks.duration(stay), Ticks.duration(fadeOut))));
    }

    /**
     * Drops the complete inventory content from a player to a specific location.
     *
     * @param player The player from which the inventory should be dropped
     */
    public static void dropPlayerInventory(@NotNull Player player) {
        Check.argCondition(player.getInstance() == null, "The instance from the player can't be null");
        dropItemStacks(player.getInstance(), player.getPosition(), player.getInventory().getItemStacks());
    }

    /**
     * Drops a certain amount of items to a given location.
     *
     * @param content The items stored in an array
     */
    public static void dropItemStacks(@NotNull Instance instance, @NotNull Pos pos, @NotNull ItemStack @NotNull ... content) {
        Check.argCondition(content.length == 0, "The array can not be null or empty");
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

    /**
     * Choose a random player from all players who are currently online.
     *
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
     *
     * @param player       The {@link Player} who receives the new equipment
     * @param hotBarItems  The hot bar items as array
     * @param locale       The {@link Locale} for {@link TranslatedItem}
     * @param shiftedSlots An array with contains shifted layout only for the hotbar
     */
    public static void updateHotBar(@NotNull Player player, @NotNull IItem[] hotBarItems, @Nullable Locale locale, int... shiftedSlots) {
        Check.argCondition(hotBarItems.length > InventoryConstants.INVENTORY_WIDTH, "The array length for the items is greater than " + InventoryConstants.INVENTORY_WIDTH);
        Check.argCondition(shiftedSlots.length > hotBarItems.length, "The length from shiftedSlots has not the same length with the underlying array");
        if (placer == null) {
            placer = ItemPlacer.FALLBACK;
            PLAYER_LOGGER.info("Set `ItemPlacer Interface` to fallback implementation");
        }
        setItems(player, hotBarItems, locale, shiftedSlots);
    }

    /**
     * Updates the armor items from a given {@link Player}.
     * The locale and shiftedSlot parameter can be null
     *
     * @param player     The {@link Player} who receives the new equipment
     * @param armorItems The array with the items for the armor area
     * @param locale     The {@link Locale} for {@link TranslatedItem}
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
     *
     * @param player       the player who should get the items
     * @param items        the array with the items
     * @param locale       the locale if the case need some
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
     *
     * @param players A list which contains some player objects
     * @return a random player
     */
    public static Optional<Player> getRandomPlayer(@NotNull List<Player> players) {
        if (players.isEmpty()) return Optional.empty();
        return Optional.of(players.get(ThreadLocalRandom.current().nextInt(players.size())));
    }

    /**
     * Send a {@link SetCooldownPacket} to a given {@link Player}.
     *
     * @param player    the player who should receive the packet
     * @param itemStack the involved {@link ItemStack}
     * @param ticks     how long the cooldown is
     */
    public static void sendCooldown(@NotNull Player player, @NotNull ItemStack itemStack, int ticks) {
        sendCooldown(player, itemStack.material(), ticks);
    }

    /**
     * Send a {@link SetCooldownPacket} to a given {@link Player}.
     *
     * @param player   the player who should receive the packet
     * @param material the {@link Material} to get the id from it2
     * @param ticks    how long the cooldown is
     */
    public static void sendCooldown(@NotNull Player player, @NotNull Material material, int ticks) {
        player.sendPacket(new SetCooldownPacket(material.name(), ticks));
    }
}