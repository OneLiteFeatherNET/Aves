package de.icevizion.aves.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.UUID;

/**
 * With the SkullBuilder, player heads can be easily created with a custom texture.
 * @author theEvilReaper
 * @version 1.0.1
 * @since 1.0.0
 */

public final class SkullBuilder extends ItemBuilder {

    private final SkullMeta skullMeta;

    /**
     * Creates a new object from the {@link SkullBuilder}.
     * In addition, some variables are created so that the builder works properly.
     */

    public SkullBuilder() {
        super(Material.PLAYER_HEAD);
        skullMeta = getItemMeta();
    }

    /**
     * Add a skin to a profile
     * @param skinValue The skin value to add
     * @param skinSignature The skin signature to add
     * @return
     */

    public SkullBuilder setSkinOverValues(String skinValue, String skinSignature) {
        Objects.requireNonNull(skinValue, "SkinValue can not be null");
        Objects.requireNonNull(skinSignature, "SkinSignature can not be null");
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property( "textures", skinValue, skinSignature));
        setSkinOverGameProfile(gameProfile);
        return this;
    }

    /**
     * Add a skin to a profile
     * @param skinValue The skin value to add
     * @return
     */

    public SkullBuilder setSkinOverValue(String skinValue) {
        Objects.requireNonNull(skinValue, "SkinValue can not be null");
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property( "textures", skinValue));
        setSkinOverGameProfile(gameProfile);
        return this;
    }

    /**
     * Invokes a {@link GameProfile} into the {@link SkullMeta}.
     * @param gameProfile The profile to invoke
     */

    private void setSkinOverGameProfile(GameProfile gameProfile) {
        Objects.requireNonNull(gameProfile, "Profile can not be null");
        try {
            Field field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullMeta, gameProfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        stack.setItemMeta(skullMeta);
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