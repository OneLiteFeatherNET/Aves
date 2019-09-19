package de.icevizion.aves.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.UUID;

public final class CustomPlayerHeadBuilder extends ItemBuilder {

    public CustomPlayerHeadBuilder() {
        super(Material.PLAYER_HEAD);
    }

    /**
     * Add a skin to a profile
     * @param skinValue The skin value to add
     * @param skinSignature The skin signature to add
     * @return
     */

    public CustomPlayerHeadBuilder setSkinOverValues(final String skinValue, final String skinSignature) {
        Objects.requireNonNull(skinValue, "SkinValue can not be null"); 
        Objects.requireNonNull(skinSignature, "SkinSignature can not be null");
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().removeAll( "textures");
        gameProfile.getProperties().put("textures", new Property( "textures", skinValue, skinSignature));
        setSkinOverGameProfile(gameProfile);
        return this;
    }

    /**
     * Set a profile to a player head
     * @param gameProfile The profile to add
     * @return
     */

    public CustomPlayerHeadBuilder setSkinOverGameProfile(GameProfile gameProfile) {
        Objects.requireNonNull(gameProfile, "Profile can not be null");
        SkullMeta meta = getItemMeta();
        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, gameProfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        stack.setItemMeta(meta);
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