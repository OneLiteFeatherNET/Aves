package de.icevizion.aves.item;

import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public final class BannerBuilder extends ItemBuilder {

    private final BannerMeta bannerMeta;

    public BannerBuilder(ItemStack itemStack) {
        super(itemStack.getType());
        bannerMeta = getItemMeta();
    }

    public static BannerBuilder of(ItemStack stack) {
        return new BannerBuilder(stack);
    }

    public BannerBuilder addPattern(int i, Pattern pattern) {
        bannerMeta.setPattern(i, pattern);
        stack.setItemMeta(bannerMeta);
        return this;
    }

    public BannerBuilder addPattern(List<Pattern> patterns) {
        bannerMeta.setPatterns(patterns);
        stack.setItemMeta(bannerMeta);
        return this;
    }

    @Override
    protected BannerMeta getItemMeta() {
        return (BannerMeta) super.getItemMeta();
    }
}
