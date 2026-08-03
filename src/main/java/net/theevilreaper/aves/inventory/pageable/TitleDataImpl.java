package net.theevilreaper.aves.inventory.pageable;

import net.kyori.adventure.text.Component;

/**
 * Implementation of the {@link TitleData} interface.
 *
 * @param title           the title of the inventory
 * @param showPageNumbers if the page numbers should be displayed
 * @param pageMapper      the mapper to map the current and max page number to a {@link Component}
 * @author theEvilReaper
 * @version 1.1.0
 * @since 1.6.0
 */
record TitleDataImpl(
        Component title,
        boolean showPageNumbers,
        TitleMapper pageMapper
) implements TitleData {
}
