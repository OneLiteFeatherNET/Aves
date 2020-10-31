package de.icevizion.aves.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class PotionBuilder extends ItemBuilder {

    public PotionBuilder(PotionType potionType) {
        super(potionType.getMaterial());
    }

    public PotionBuilder(ItemStack stack) {
        super(stack.getType());
    }

    public PotionBuilder setColor(Color color) {
        PotionMeta meta = getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return this;
    }

    public PotionBuilder setBasePotionData(PotionData potionData) {
        PotionMeta meta = getItemMeta();
        meta.setBasePotionData(potionData);
        stack.setItemMeta(meta);
        return this;
    }

    public PotionBuilder addEffect(PotionEffect effect) {
        PotionMeta meta = getItemMeta();
        meta.addCustomEffect(effect, false);
        stack.setItemMeta(meta);
        return this;
    }

    public PotionBuilder addEffect(PotionEffect effect, boolean overwriteMeta) {
        PotionMeta meta = getItemMeta();
        meta.addCustomEffect(effect, overwriteMeta);
        stack.setItemMeta(meta);
        return this;
    }

    public PotionBuilder removeEffect(PotionEffect effect) {
        PotionMeta meta = getItemMeta();
        meta.removeCustomEffect(effect.getType());
        stack.setItemMeta(meta);
        return this;
    }

    public PotionBuilder removeEffect(PotionEffectType type) {
        PotionMeta meta = getItemMeta();
        meta.removeCustomEffect(type);
        stack.setItemMeta(meta);
        return this;
    }


    @Override
    protected PotionMeta getItemMeta() {
        return (PotionMeta) super.getItemMeta();
    }

    public enum PotionType {

        POTION(Material.POTION),
        SPLASH(Material.SPLASH_POTION),
        LINGERING(Material.LINGERING_POTION);

        private final Material material;

        PotionType(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
}