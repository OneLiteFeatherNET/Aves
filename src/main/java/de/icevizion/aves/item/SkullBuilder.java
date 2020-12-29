package de.icevizion.aves.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;
import java.util.UUID;

public final class SkullBuilder extends ItemBuilder {

    public SkullBuilder() {
        super(Material.PLAYER_HEAD);
    }

    /**
     * Add a skin to a profile
     * @param skinValue The skin value to add
     * @param skinSignature The skin signature to add
     * @return
     */

    public SkullBuilder setSkinOverValues(UUID uuid, String name, String skinValue, String skinSignature) {
        Objects.requireNonNull(skinValue, "SkinValue can not be null"); 
        Objects.requireNonNull(skinSignature, "SkinSignature can not be null");
        PlayerProfile profile = Bukkit.createProfile(uuid);
        profile.setProperty(new ProfileProperty(name, skinValue, skinSignature));
        SkullMeta skullMeta = getItemMeta();
        skullMeta.setPlayerProfile(profile);
        stack.setItemMeta(skullMeta);
        return this;
    }

    /**
     * Add a skin to a profile
     * @param skinValue The skin value to add
     * @return
     */

    public SkullBuilder setSkinOverValues(String name, String skinValue) {
        Objects.requireNonNull(skinValue, "SkinValue can not be null");
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty(name, skinValue));
        SkullMeta skullMeta = getItemMeta();
        skullMeta.setPlayerProfile(profile);
        stack.setItemMeta(skullMeta);
        return this;
    }

    /**
     * Override the default method to return another meta
     * @return The {@link SkullMeta}
     */

    @Override
    protected SkullMeta getItemMeta() {
        return (SkullMeta) super.getItemMeta();
    }
}