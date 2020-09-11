package de.icevizion.aves.item;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.material.SpawnEgg;

public final class SpawnEggBuilder extends ItemBuilder {

    public SpawnEggBuilder() {
        super(Material.MONSTER_EGG);
    }

    /**
     * Set the type of the spawn egg.
     *
     * @param type The typ of the egg
     * @return
     */

    public SpawnEggBuilder setType(EntityType type) {
        SpawnEgg egg = new SpawnEgg(type);
        stack.setData(egg.toItemStack().getData());
        return this;
    }
}