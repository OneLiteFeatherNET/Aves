package de.cosmiqglow.aves.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public final class PotionBuilder extends ItemBuilder {

    public PotionBuilder(PotionItemType potionType) {
        super(potionType.getMaterial());
    }

    public PotionBuilder setColor(Color color) {
        PotionMeta meta = getItemMeta();
        meta.setColor(color);
        this.stack.setItemMeta(meta);
        return this;
    }

    public PotionBuilder setPotionData(PotionType potionType) {
        PotionMeta meta = getItemMeta();
        meta.setBasePotionData(new PotionData(potionType));
        this.stack.setItemMeta(meta);
        return this;
    }

    public PotionBuilder setPotionData(PotionType potionType, boolean extended, boolean upgrade) {
        PotionMeta meta = getItemMeta();
        meta.setBasePotionData(new PotionData(potionType, extended, upgrade));
        this.stack.setItemMeta(meta);
        return this;
    }

    public PotionBuilder setPotionEffect(PotionEffect potionEffect, boolean overwrite) {
        PotionMeta meta = getItemMeta();
        meta.addCustomEffect(potionEffect, overwrite);
        this.stack.setItemMeta(meta);
        return this;
    }

    @Override
    protected PotionMeta getItemMeta() {
        return (PotionMeta) super.getItemMeta();
    }

    public enum PotionItemType {

        POTION(Material.POTION),
        SPLASH_POTION(Material.SPLASH_POTION),
        LINGERING_POTION(Material.LINGERING_POTION),
        TIPPED_ARROW(Material.TIPPED_ARROW);

        final Material material;

        PotionItemType(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
}