package de.icevizion.aves.item;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public final class FireworkBuilder extends ItemBuilder {

    public FireworkBuilder() {
        super(Material.FIREWORK);
    }

    /**
     * Add another effect to this firework
     * @param effect The effect to add
     * @return
     */

    public FireworkBuilder addEffect(FireworkEffect effect) {
        FireworkMeta meta = getItemMeta();
        meta.addEffect(effect);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Add several effects to this firework
     * @param effects The effects to add
     * @return
     */

    public FireworkBuilder addEffects(FireworkEffect... effects) {
        FireworkMeta meta = getItemMeta();
        meta.addEffects(effects);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the approximate power of the firework. Each level of power is half a second of flight time
     * @param power The power of the firework
     * @return
     */

    public FireworkBuilder setPower(int power) {
        if (power > 128) {
            throw new IllegalArgumentException("The power can nit be higher than 128");
        }
        FireworkMeta meta = getItemMeta();
        meta.setPower(power);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Spawn a firework
     * @param location The location where the firework spawns
     * @return
     */

    public Firework spawn(Location location) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        fw.setFireworkMeta(getItemMeta());
        return fw;
    }

    /**
     * Override the default method to return another meta
     * @return The {@link FireworkMeta}
     */

    @Override
    protected FireworkMeta getItemMeta() {
        return (FireworkMeta) super.getItemMeta();
    }
}