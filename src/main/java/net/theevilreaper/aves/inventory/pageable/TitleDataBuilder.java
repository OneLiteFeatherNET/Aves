package net.theevilreaper.aves.inventory.pageable;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

/**
 * The @{TitleDataBuilder} is the implementation of the {@link TitleData.Builder} interface.
 * It provides the possibility to build a {@link TitleData} instance.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @see TitleData
 * @since 1.6.0
 */
public final class TitleDataBuilder implements TitleData.Builder {

    private @Nullable Component title;
    private boolean showPageNumbers;
    private TitleMapper pageMapper;

    TitleDataBuilder() {
        this.pageMapper = TitleMapper.DEFAULT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TitleData.Builder title(Component title) {
        this.title = title;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TitleData.Builder showPageNumbers(boolean showPageNumbers) {
        this.showPageNumbers = showPageNumbers;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TitleData.Builder pageMapper(TitleMapper pageMapper) {
        this.pageMapper = pageMapper;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TitleData build() {
        return new TitleDataImpl(this.title, this.showPageNumbers, this.pageMapper);
    }
}
