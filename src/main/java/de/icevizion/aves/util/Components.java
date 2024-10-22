package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static de.icevizion.aves.util.Strings.UTF_8_HEART;

/**
 * The class contains some usefully methods for {@link Component} from adventure.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@ApiStatus.NonExtendable
public final class Components {

    public static final TextColor FILLED_HEART = NamedTextColor.RED;
    public static final TextColor EMPTY_HEARTS = NamedTextColor.GRAY;

    private Components() {
    }

    /**
     * Creates a progress bar for the given values.
     *
     * @param current           the current amount
     * @param max               the maximum amount
     * @param totalBars         the maximum bars to display
     * @param symbol            the symbol to display
     * @param completedColor    the color for the completed part
     * @param notCompletedColor the color for the not completed part
     * @return the progressbar as string
     */
    @Contract(pure = true)
    public static @NotNull Component getProgressBar(int current,
                                                    int max,
                                                    int totalBars,
                                                    @NotNull String symbol,
                                                    @NotNull TextColor completedColor,
                                                    @NotNull TextColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);
        String completedCharAsString = symbol.repeat(progressBars);
        String nonCompletedCharAsString = symbol.repeat(totalBars - progressBars);
        return Component.text(completedCharAsString, completedColor).append(Component.text(nonCompletedCharAsString, notCompletedColor));
    }

    /**
     * Converts the life of a player into a string. Full hearts are displayed in red and empty hearts in grey.
     *
     * @param paramHealth     the health of a player
     * @param remainingHearth the color for the remaining hearths
     * @param goneHearth      the color for the hearth which are gone
     * @return the converted health as string
     */
    @Contract(value = "_, _, _ -> new ", pure = true)
    public static @NotNull Component getHealthString(double paramHealth,
                                                     @NotNull TextColor remainingHearth,
                                                     @NotNull TextColor goneHearth) {
        int health = (int) Math.round(paramHealth);
        health /= 2;
        int healthAway = 10 - health;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < health; i++) {
            builder.append(remainingHearth).append(UTF_8_HEART);
        }
        for (int i = 0; i < healthAway; i++) {
            builder.append(goneHearth).append(UTF_8_HEART);
        }
        return LegacyComponentSerializer.legacySection().deserialize(builder.toString());
    }

    /**
     * Converts the life of a player into a string. Full hearts are displayed in red and empty hearts in grey.
     *
     * @param paramHealth the health of a player
     * @return the converted health as string
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Component getHealthString(double paramHealth) {
        return getHealthString(paramHealth, FILLED_HEART, EMPTY_HEARTS);
    }

    /**
     * Creates a textual representation of an implementation of a {@link Point}.
     * The method uses the standard {@link JoinConfiguration#arrayLike()} definition from adventure itself.
     *
     * @param pos the involved point
     * @param <T> the point implementation class
     * @return the textual representation mapped as {@link Component}
     */
    @Contract(value = "_ -> new", pure = true)
    public static <T extends Point> @NotNull Component convertPoint(@NotNull T pos) {
        return convert(pos, JoinConfiguration.arrayLike());
    }

    /**
     * Creates a textual representation of an implementation of a {@link Point}.
     *
     * @param pos               the involved point
     * @param joinConfiguration the {@link JoinConfiguration} to map the entries
     * @param <T>               the point implementation class
     * @return the textual representation mapped as {@link Component}
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <T extends Point> @NotNull Component convertPoint(@NotNull T pos, @NotNull JoinConfiguration joinConfiguration) {
        return convert(pos, joinConfiguration);
    }

    /**
     * Creates a textual representation of an implementation of a {@link Point}.
     *
     * @param pos                  the involved point
     * @param configurationBuilder the {@link JoinConfiguration.Builder} to map the entries
     * @param <T>                  the point implementation class
     * @return the textual representation mapped as {@link Component}
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <T extends Point> @NotNull Component convertPoint(@NotNull T pos, @NotNull JoinConfiguration.Builder configurationBuilder) {
        return convert(pos, configurationBuilder.build());
    }

    /**
     * Creates a textual representation of an implementation of a {@link Point}.
     *
     * @param point             the involved point
     * @param joinConfiguration the {@link JoinConfiguration} to map the entries
     * @param <T>               the point implementation class
     * @return the textual representation mapped as {@link Component}
     */
    @Contract(value = "_, _ -> new", pure = true)
    private static <T extends Point> @NotNull Component convert(@NotNull T point, @NotNull JoinConfiguration joinConfiguration) {
        final boolean isPos = point instanceof Pos;
        Component[] data = new Component[isPos ? 5 : 3];
        data[0] = Component.text("x:").append(Component.space()).append(Component.text(point.blockX()));
        data[1] = Component.text("y:").append(Component.space()).append(Component.text(point.blockY()));
        data[2] = Component.text("z:").append(Component.space()).append(Component.text(point.blockZ()));

        if (isPos) {
            final Pos pos = (Pos) point;
            data[3] = Component.text("yaw:").append(Component.space()).append(Component.text(pos.yaw()));
            data[4] = Component.text("pitch:").append(Component.space()).append(Component.text(pos.pitch()));
        }
        return Component.join(joinConfiguration, data);
    }

    /**
     * Converts a {@link Point} to a list of {@link Component} which represents the lore.
     * The method has a given color scheme which can't be changed.
     *
     * @param miniMessage                    the mini message instance
     * @param point                          the point to convert
     * @param format                         the decimal format to format the values
     * @param <T>                            the point implementation class
     * @return the list of components
     */
    @Contract(value = "_, _, _-> new", pure = true)
    public static <T extends Point> @NotNull @UnmodifiableView List<@NotNull Component> pointToLore(
            @NotNull MiniMessage miniMessage,
            @NotNull T point,
            @NotNull DecimalFormat format
    ) {
        final List<Component> loreComponents = new ArrayList<>();
        loreComponents.add(miniMessage.deserialize("<!i><gray>X: <white>" + format.format(point.x())));
        loreComponents.add(miniMessage.deserialize("<!i><gray>Y: <white>" + format.format(point.y())));
        loreComponents.add(miniMessage.deserialize("<!i><gray>Z: <white>" + format.format(point.z())));
        if (point instanceof Pos pos) {
            loreComponents.add(miniMessage.deserialize("<!i><gray>Yaw: <white>" + format.format(pos.yaw())));
            loreComponents.add(miniMessage.deserialize("<!i><gray>Pitch: <white>" + format.format(pos.pitch())));
        }
        return loreComponents;
    }

    /**
     * Converts a {@link Point} to a list of {@link Component} which represents the lore.
     *
     * @param point                          the point to convert
     * @param format                         the decimal format to format the values
     * @param <T>                            the point implementation class
     * @param partArguments                  the components for the values
     * @return the list of components
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T extends Point> @NotNull List<@NotNull Component> pointToLore(
            @NotNull T point,
            @NotNull DecimalFormat format,
            @NotNull List<Component> partArguments
    ) {
        final int maxSize = point instanceof Pos ? 5 : 3;
        if (partArguments.size() != maxSize) {
            throw new IllegalArgumentException("""
                    The size of the argument parts doesn't match with the given point implementation.
                    Given point impl is %s which requires %s arguments and the size of the partArguments is %s
                    """.formatted(point.getClass().getSimpleName(), maxSize, partArguments.size())
            );
        }

        final List<Component> loreComponents = new ArrayList<>(partArguments.size());
        loreComponents.add(partArguments.getFirst().append(Component.text(format.format(point.x()))));
        loreComponents.add(partArguments.get(1).append(Component.text(format.format(point.y()))));
        loreComponents.add(partArguments.get(2).append(Component.text(format.format(point.z()))));
        if (point instanceof Pos pos) {
            loreComponents.add(partArguments.get(3).append(Component.text(format.format(pos.yaw()))));
            loreComponents.add(partArguments.get(4).append(Component.text(format.format(pos.pitch()))));
        }
        return loreComponents;
    }
}
