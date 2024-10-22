package de.icevizion.aves.inventory.pageable;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

/**
 * Implementation of the {@link TitleData} interface.
 *
 * @param title           the title of the inventory
 * @param showPageNumbers if the page numbers should be displayed
 * @param pageMapper      the mapper to map the current and max page number to a {@link Component}
 */
record TitleDataImpl(
        @NotNull Component title,
        boolean showPageNumbers,
        @Nullable TitleMapper pageMapper
) implements TitleData {
}
