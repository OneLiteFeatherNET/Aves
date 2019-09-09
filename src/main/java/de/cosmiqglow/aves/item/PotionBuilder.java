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

    /**
     * Sets the potion color
     * @param color The color to set
     * @return
     */

    public PotionBuilder setColor(Color color) {
        PotionMeta meta = getItemMeta();
        meta.setColor(color);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the underlying potion data
     * @param potionType The type to add
     * @return
     */

    public PotionBuilder setPotionData(PotionType potionType) {
        PotionMeta meta = getItemMeta();
        meta.setBasePotionData(new PotionData(potionType));
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the underlying potion data
     * @param potionType The type to add
     * @param extended whether the potion is extended PotionType#isExtendable() must be true
     * @param upgrade whether the potion is upgraded PotionType#isUpgradable() must be true
     * @return
     */

    public PotionBuilder setPotionData(PotionType potionType, boolean extended, boolean upgrade) {
        PotionMeta meta = getItemMeta();
        meta.setBasePotionData(new PotionData(potionType, extended, upgrade));
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Adds a custom potion effect to this potion
     * @param potionEffect The potion effect to add
     * @param overwrite True if any existing effect of the same type should be overwritten
     * @return
     */

    public PotionBuilder addPotionEffect(PotionEffect potionEffect, boolean overwrite) {
        PotionMeta meta = getItemMeta();
        meta.addCustomEffect(potionEffect, overwrite);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Override the default method to return another meta
     * @return The {@link PotionMeta}
     */

    @Override
    protected PotionMeta getItemMeta() {
        return (PotionMeta) super.getItemMeta();
    }

    /**
     * The enum is a wrapper for the existing potion material
     */

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