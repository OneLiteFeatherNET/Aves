package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static de.icevizion.aves.util.Strings.UTF_8_HEART;

/**
 * The class contains some usefully methods for {@link Component} from adventure.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@ApiStatus.Experimental
public final class Components {

    public static final NamedTextColor FILLED_HEART = NamedTextColor.RED;
    public static final NamedTextColor EMPTY_HEARTS = NamedTextColor.GRAY;

    private Components() {}

    /**
     * Creates a progress bar for the given values.
     * @param current The current amount
     * @param max The maximum amount
     * @param totalBars The maximum bars to display
     * @param symbol The symbol to display
     * @param completedColor The color for the completed part
     * @param notCompletedColor The color for the not completed part
     * @return The progressbar as string
     */
    @Contract(pure = true)
    public static @NotNull Component getProgressBar(int current,
                                                    int max,
                                                    int totalBars,
                                                    @NotNull String symbol,
                                                    @NotNull NamedTextColor completedColor,
                                                    @NotNull NamedTextColor notCompletedColor) {

        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);
        String completedCharAsString = symbol.repeat(progressBars);
        String nonCompletedCharAsString = symbol.repeat(totalBars - progressBars);
        return Component.text(completedCharAsString, completedColor).append(Component.text(nonCompletedCharAsString, notCompletedColor));
    }

    /**
     * Converts the life of a player into a string. Full hearts are displayed in red and empty hearts in grey.
     * @param paramHealth The health of a player
     * @param remainingHearth The color for the remaining hearths
     * @param goneHearth The color for the hearth which are gone
     * @return The converted health as string
     */
    @NotNull
    public static Component getHealthString(double paramHealth,
                                            @NotNull NamedTextColor remainingHearth,
                                            @NotNull NamedTextColor goneHearth) {
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
     * @param paramHealth The health of a player
     * @return The converted health as string
     */
    @NotNull
    public static Component getHealthString(double paramHealth) {
        return getHealthString(paramHealth, FILLED_HEART, EMPTY_HEARTS);
    }
}
