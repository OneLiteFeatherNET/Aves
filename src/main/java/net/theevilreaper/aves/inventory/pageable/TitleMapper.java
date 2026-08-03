package net.theevilreaper.aves.inventory.pageable;

import net.kyori.adventure.text.Component;

/**
 * The {@link TitleMapper} is a functional interface to map the title of the inventory.
 * This interface is used in the {@link PageableInventory} to define the title of the inventory.
 * The title can be changed based on the current and max page.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 1.6.0
 */
@FunctionalInterface
public interface TitleMapper {

    TitleMapper DEFAULT = (current, max) -> Component.text(" (" + current + "/" + max + ")");

    /**
     * Maps the title of the inventory.
     *
     * @param currentPage the current page
     * @param maxPage     the max page
     * @return the mapped title
     */
    Component apply(int currentPage, int maxPage);
}
