package de.icevizion.aves.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public final class BannerBuilder extends ItemBuilder {

    public BannerBuilder() {
        super(Material.BANNER);
    }

    public BannerBuilder(ItemStack itemStack) {
        super(itemStack.getType());
    }

    public BannerBuilder setColor(DyeColor color) {
        BannerMeta meta = getItemMeta();
        meta.setBaseColor(color);
        stack.setItemMeta(meta);
        return this;
    }

    public BannerBuilder addPattern(int i, Pattern pattern) {
        BannerMeta meta = getItemMeta();
        meta.setPattern(i, pattern);
        stack.setItemMeta(meta);
        return this;
    }

    public BannerBuilder addPattern(List<Pattern> patterns) {
        BannerMeta meta = getItemMeta();
        meta.setPatterns(patterns);
        stack.setItemMeta(meta);
        return this;
    }

    @Override
    protected BannerMeta getItemMeta() {
        return (BannerMeta) super.getItemMeta();
    }
}
