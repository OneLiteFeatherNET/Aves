package de.icevizion.aves.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public final class PotionBuilder extends ItemBuilder {

    private final Potion potion;
    private final List<PotionEffect> effects;

    public PotionBuilder() {
        super(Material.POTION);
        potion = new Potion(PotionType.WATER);
        effects = new ArrayList<>();
    }

    public PotionBuilder setSplash(boolean splash) {
        potion.setSplash(splash);
        return this;
    }

    public PotionBuilder addEffect(PotionEffect effect) {
        effects.add(effect);
        return this;
    }

    @Override
    public ItemStack build() {
        ItemStack stack = potion.toItemStack(this.stack.getAmount());
        ItemMeta oldMeta = this.stack.getItemMeta();
        PotionMeta meta = (PotionMeta) stack.getItemMeta();

        meta.setLore(oldMeta.getLore());
        meta.setDisplayName(oldMeta.getDisplayName());
        stack.addEnchantments(oldMeta.getEnchants());
        effects.forEach(eff -> meta.addCustomEffect(eff, false));
        oldMeta.getItemFlags().forEach(meta::addItemFlags);

        stack.setItemMeta(meta);
        return stack;
    }
}