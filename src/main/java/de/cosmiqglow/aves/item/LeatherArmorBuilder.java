package de.cosmiqglow.aves.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public final class LeatherArmorBuilder extends ItemBuilder {

    public LeatherArmorBuilder(LeatherArmorType type) {
        super(type.getMaterial());
    }

    public LeatherArmorBuilder setColor(Color color) {
        LeatherArmorMeta meta = getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return this;
    }

    public LeatherArmorBuilder setColor(int red, int green, int blue) {
        LeatherArmorMeta meta = getItemMeta();
        meta.setColor(Color.fromRGB(red, green, blue));
        stack.setItemMeta(meta);
        return this;
    }

    @Override
    protected LeatherArmorMeta getItemMeta() {
        return (LeatherArmorMeta) super.getItemMeta();
    }

    public enum LeatherArmorType {
        LEATHER_HELMET(Material.LEATHER_HELMET),
        LEATHER_CHESTPLATE(Material.LEATHER_CHESTPLATE),
        LEATHER_LEGGINGS(Material.LEATHER_LEGGINGS),
        LEATHER_BOOTS(Material.LEATHER_BOOTS);

        final Material material;

        LeatherArmorType(final Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
}