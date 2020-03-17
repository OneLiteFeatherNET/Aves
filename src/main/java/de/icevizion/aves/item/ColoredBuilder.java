package de.icevizion.aves.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ColoredBuilder extends ItemBuilder {

    private final DyeType type;

    public ColoredBuilder(DyeType type) {
        super(type.getMaterial());
        this.type = type;
    }

    public ColoredBuilder(ItemStack itemStack) {
        super(itemStack.getType());
        this.type = getType(itemStack);
    }

    @SuppressWarnings("deprecation")
    public ColoredBuilder setColor(DyeColor dyeColor) {
        ItemStack item = new ItemStack(stack.getType(), stack.getAmount(), type.usesWoolID() ? dyeColor.getWoolData() : dyeColor.getDyeData());
        item.setItemMeta(stack.getItemMeta());
        this.stack = item;
        return this;
    }

    private DyeType getType(ItemStack itemStack) {
        for (DyeType type : DyeType.values()) {
            if (itemStack.getType() == type.getMaterial())
            return type;
        }
        return null;
    }

    public enum DyeType {

        DYE(Material.INK_SACK, false),
        WOOL_BLOCK(Material.WOOL, true),
        WOOL_CARPET(Material.CARPET, true),
        CLAY_BLOCK(Material.STAINED_CLAY, true),
        CLAY_BLOCK_HARD(Material.HARD_CLAY, true),
        GLASS_BLOCK(Material.STAINED_GLASS, true),
        GLASS_PANE(Material.STAINED_GLASS_PANE, true);

        final Material material;
        final boolean woolID;

        DyeType(Material material, boolean woolID) {
            this.material = material;
            this.woolID = woolID;
        }

        public boolean usesWoolID() {
            return this.woolID;
        }

        public Material getMaterial() {
            return this.material;
        }
    }
}