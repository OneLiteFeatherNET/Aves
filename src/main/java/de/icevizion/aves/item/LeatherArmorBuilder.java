package de.icevizion.aves.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public final class LeatherArmorBuilder extends ItemBuilder {

    public LeatherArmorBuilder(LeatherType type) {
        super(type.getMaterial());
    }

    public LeatherArmorBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    /**
     * Sets the color of the armor
     * @param color The color to set
     * @return
     */

    public LeatherArmorBuilder setColor(Color color) {
        LeatherArmorMeta meta = getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the color of the armor with RGB values
     * @param red The amount of red
     * @param green The amount of green
     * @param blue The amount of blue
     * @return
     */

    public LeatherArmorBuilder setColor(int red, int green, int blue) {
        LeatherArmorMeta meta = getItemMeta();
        meta.setColor(Color.fromRGB(red, green, blue));
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Override the default method to return another meta
     * @return The {@link LeatherArmorMeta}
     */

    @Override
    protected LeatherArmorMeta getItemMeta() {
        return (LeatherArmorMeta) super.getItemMeta();
    }

    /**
     * The enum is a wrapper for each armor from the type leather
     */

    public enum LeatherType {
        HELMET(Material.LEATHER_HELMET),
        CHESTPLATE(Material.LEATHER_CHESTPLATE),
        LEGGINGS(Material.LEATHER_LEGGINGS),
        BOOTS(Material.LEATHER_BOOTS);

        final Material material;

        LeatherType(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
}