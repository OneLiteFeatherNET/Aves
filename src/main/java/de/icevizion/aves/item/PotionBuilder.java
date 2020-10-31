package de.icevizion.aves.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class PotionBuilder extends ItemBuilder {

    private final PotionMeta potionMeta;

    public PotionBuilder(PotionType potionType) {
        super(potionType.getMaterial());
        potionMeta = getItemMeta();
    }

    public PotionBuilder(ItemStack stack) {
        super(stack.getType());
        potionMeta = getItemMeta();
    }

    public PotionBuilder setColor(Color color) {
        potionMeta.setColor(color);
        stack.setItemMeta(potionMeta);
        return this;
    }

    public PotionBuilder setBasePotionData(PotionData potionData) {
        potionMeta.setBasePotionData(potionData);
        stack.setItemMeta(potionMeta);
        return this;
    }

    public PotionBuilder addEffect(PotionEffect effect) {
        potionMeta.addCustomEffect(effect, false);
        stack.setItemMeta(potionMeta);
        return this;
    }

    public PotionBuilder addEffect(PotionEffect effect, boolean overwriteMeta) {
        potionMeta.addCustomEffect(effect, overwriteMeta);
        stack.setItemMeta(potionMeta);
        return this;
    }

    public PotionBuilder removeEffect(PotionEffect effect) {
        PotionMeta meta = getItemMeta();
        meta.removeCustomEffect(effect.getType());
        stack.setItemMeta(meta);
        return this;
    }

    public PotionBuilder removeEffect(PotionEffectType type) {
        potionMeta.removeCustomEffect(type);
        stack.setItemMeta(potionMeta);
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