package de.cosmiqglow.aves.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;

public final class ItemStorageBuilder extends ItemBuilder {

    private final NamespacedKey namespacedKey;

    public ItemStorageBuilder(Material material, Plugin plugin, String keyName) {
        super(material);
        this.namespacedKey = new NamespacedKey(plugin, keyName);
    }

    public ItemStorageBuilder(ItemStack itemStack, Plugin plugin, String keyName) {
        super(itemStack);
        this.namespacedKey = new NamespacedKey(plugin, keyName);
    }

    public ItemStorageBuilder setStorageData(final ItemTagType tagType, final Object value) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.getCustomTagContainer().setCustomTag(namespacedKey, tagType, value);
        stack.setItemMeta(itemMeta);
        return this;
    }
}