package de.cosmiqglow.aves.item;

import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemAttributeBuilder extends ItemBuilder {

    public ItemAttributeBuilder(Material material) {
        super(material);
    }

    public ItemAttributeBuilder addModifier(final Attribute attribute, final AttributeModifier modifier) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.addAttributeModifier(attribute, modifier);
        stack.setItemMeta(itemMeta);
        return this;
    }

    public ItemAttributeBuilder setAttributeModifiers(Multimap<Attribute, AttributeModifier> modifiersList) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setAttributeModifiers(modifiersList);
        stack.setItemMeta(itemMeta);
        return this;
    }
}
