package de.cosmiqglow.aves.item;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public final class FireworkBuilder extends ItemBuilder {

    public FireworkBuilder() {
        super(Material.FIREWORK_ROCKET);
    }

    public FireworkBuilder addEffect(FireworkEffect effect) {
        FireworkMeta meta = getItemMeta();
        meta.addEffect(effect);
        this.stack.setItemMeta(meta);
        return this;
    }

    public FireworkBuilder addEffects(FireworkEffect... effects) {
        FireworkMeta meta = getItemMeta();
        meta.addEffects(effects);
        this.stack.setItemMeta(meta);
        return this;
    }

    public FireworkBuilder addEfect(FireworkEffect.Builder builder) {
        FireworkMeta meta = getItemMeta();
        meta.addEffect(builder.build());
        stack.setItemMeta(meta);
        return this;
    }

    public FireworkBuilder setPower(int power) {
        FireworkMeta meta = getItemMeta();
        meta.setPower(power);
        this.stack.setItemMeta(meta);
        return this;
    }

    public Firework spawn(final Location location) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        fw.setFireworkMeta(getItemMeta());
        return fw;
    }

    @Override
    protected FireworkMeta getItemMeta() {
        return (FireworkMeta) super.getItemMeta();
    }
}