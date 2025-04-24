package net.theevilreaper.aves.inventory.pageable;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * The @{TitleDataBuilder} is the implementation of the {@link TitleData.Builder} interface.
 * It provides the possibility to build a {@link TitleData} instance.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @see TitleData
 * @since 1.6.0
 */
public final class TitleDataBuilder implements TitleData.Builder {

    private Component title;
    private boolean showPageNumbers;
    private TitleMapper pageMapper;

    @Override
    public TitleData.@NotNull Builder title(@NotNull Component title) {
        this.title = title;
        return this;
    }

    @Override
    public TitleData.@NotNull Builder showPageNumbers(boolean showPageNumbers) {
        this.showPageNumbers = showPageNumbers;
        return this;
    }

    @Override
    public TitleData.@NotNull Builder pageMapper(@NotNull TitleMapper pageMapper) {
        this.pageMapper = pageMapper;
        return this;
    }

    @Override
    public @NotNull TitleData build() {
        return new TitleDataImpl(this.title, this.showPageNumbers, this.pageMapper);
    }
}
