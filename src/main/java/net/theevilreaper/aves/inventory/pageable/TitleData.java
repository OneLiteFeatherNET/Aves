package net.theevilreaper.aves.inventory.pageable;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

/**
 * The {@link TitleData} interface defines how the {@link PageableInventory} has access to the title of the inventory.
 * If the title should contain the current and max page number the {@link TitleData} is the right place to define this.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @see TitleDataImpl
 * @since 1.6.0
 */
public sealed interface TitleData permits TitleDataImpl {

    /**
     * Creates a new instance of the {@link Builder} interface.
     *
     * @return the created instance
     */
    @Contract(pure = true)
    static @NotNull Builder builder() {
        return new TitleDataBuilder();
    }

    /**
     * Returns the title of the inventory.
     *
     * @return the title as {@link Component}
     */
    @NotNull Component title();

    /**
     * Returns if the page numbers should be displayed in the title.
     *
     * @return true if the page numbers should be displayed otherwise false
     */
    boolean showPageNumbers();

    /**
     * Returns the page mapper which is used to map the current and max page number to a {@link Component}.
     *
     * @return the page mapper as {@link BiFunction}
     */
    @Nullable TitleMapper pageMapper();

    /**
     * The interface defines the structure to build a {@link TitleData} instance.
     *
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.6.0
     */
    sealed interface Builder permits TitleDataBuilder {

        /**
         * Set the title for the inventory.
         *
         * @param title the title as {@link Component}
         * @return the builder instance
         */
        @NotNull Builder title(@NotNull Component title);

        /**
         * Set if the page numbers should be displayed in the title.
         *
         * @param showPageNumbers true if the page numbers should be displayed otherwise false
         * @return the builder instance
         */
        @NotNull Builder showPageNumbers(boolean showPageNumbers);

        /**
         * Set the page mapper which is used to map the current and max page number to a {@link Component}.
         *
         * @param pageMapper the page mapper as {@link TitleMapper}
         * @return the builder instance
         */
        @NotNull Builder pageMapper(@NotNull TitleMapper pageMapper);

        /**
         * Builds a new instance of the {@link TitleData} interface.
         *
         * @return the created instance
         */
        @NotNull TitleData build();
    }
}
