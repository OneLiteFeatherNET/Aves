package de.cosmiqglow.aves.item;

import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public final class BannerBuilder extends ItemBuilder {

    public BannerBuilder(Material material) {
        super(material);
    }

    public BannerBuilder addPattern(final Pattern pattern) {
        BannerMeta meta = getBannerMeta();
        meta.addPattern(pattern);
        stack.setItemMeta(meta);
        return this;
    }

    public BannerBuilder setPatterns(final List<Pattern> patterns) {
        BannerMeta meta = getBannerMeta();
        meta.setPatterns(patterns);
        stack.setItemMeta(meta);
        return this;
    }

    public BannerBuilder setPatterns(final int index, Pattern pattern) {
        BannerMeta meta = getBannerMeta();
        meta.setPattern(index, pattern);
        stack.setItemMeta(meta);
        return this;
    }

    private BannerMeta getBannerMeta() {
        return (BannerMeta) getItemMeta();
    }

    enum BannerType {

        RED_BANNER(Material.RED_BANNER),
        BLACK_BANNER(Material.BLACK_BANNER),
        BLUE_BANNER(Material.BLUE_BANNER);


        final Material material;

        BannerType(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
}
