package net.theevilreaper.aves.inventory.pageable;

/**
 * The enum contains all actions which can be applied to an inventory page.
 * With {@link PageAction#FORWARD} or {@link PageAction#BACKWARDS} trigger a page switch.
 * The {@link PageAction#REFRESH} is used to update the content from a page
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
public enum PageAction {

    REFRESH,
    FORWARD,
    BACKWARDS
}
