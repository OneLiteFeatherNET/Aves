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

    public SkullBuilder setSkinOverValues(String name, String skinValue, String skinSignature) {
        Objects.requireNonNull(skinValue, "SkinValue can not be null"); 
        Objects.requireNonNull(skinSignature, "SkinSignature can not be null");
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty(name, skinValue, skinSignature));
        getItemMeta().setPlayerProfile(profile);
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
        getItemMeta().setPlayerProfile(profile);
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

    public enum SkullType {

        SKELETON(0),
        SKELETON_WITHER(1),
        ZOMBIE(2),
        PLAYER(3),
        CREEPER(4);

        final int data;

        SkullType(int data) {
            this.data = data;
        }

        public int getData() {
            return data;
        }
    }
}