package de.icevizion.aves.item;

import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemAttributeBuilder extends ItemBuilder {

    public ItemAttributeBuilder(Material material) {
        super(material);
    }

    /**
     * Add an Attribute and it's Modifier. AttributeModifiers can now support EquipmentSlots.
     * If not set, the AttributeModifier will be active in ALL slots
     * @param attribute The Attribute to modify
     * @param modifier The AttributeModifier specifying the modification
     * @return
     */

    public ItemAttributeBuilder addModifier(final Attribute attribute, final AttributeModifier modifier) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.addAttributeModifier(attribute, modifier);
        stack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Set all Attributes and their AttributeModifiers.
     * To clear all currently set Attributes and AttributeModifiers use null or an empty Multimap.
     * If not null nor empty, this will filter all entries that are not-null and add them to the ItemStack.
     * @param modifiersList The new Multimap containing the Attributes and their AttributeModifiers
     * @return
     */

    public ItemAttributeBuilder setAttributeModifiers(Multimap<Attribute, AttributeModifier> modifiersList) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setAttributeModifiers(modifiersList);
        stack.setItemMeta(itemMeta);
        return this;
    }
}
