package de.icevizion.aves.decoder;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.spi.Decoder;

import java.io.IOException;

public final class ItemStackDecoder implements Decoder {

    @Override
    public Object decode(JsonIterator jsonIterator) throws IOException {
        Any any = jsonIterator.readAny();

     /*   Material material = Material.getMaterial(any.get("material").toString());

        if (material == null) {
            material = Material.STONE;
        }

        ItemBuilder itemBuilder = new ItemBuilder(material);

        itemBuilder.setAmount(any.get("amount").toInt());
        if (any.get("meta").valueType() == ValueType.INVALID) {
            return itemBuilder.build();
        }

        Any meta = any.get("meta");

        //.valueType() != ValueType.INVALID check if the given value is present

        if (meta.get("displayName").valueType() != ValueType.INVALID) {
            itemBuilder.setDisplayName(meta.get("displayName").toString());
        }

        if (meta.get("enchantments").valueType() != ValueType.INVALID) {
            List<Any> enchantments = meta.get("enchantments").asList();
            for (Any enchantment : enchantments) {
                Enchantment en = Enchantment.getByKey(NamespacedKey.minecraft(enchantment.get("enchantment").toString()));
                itemBuilder.addEnchantment(en, enchantment.get("level").toInt(), true);
            }
        }
        if (meta.get("lore").valueType() != ValueType.INVALID) {
            List<String> lore = new ArrayList<>();
            for (Any loreElement : meta.get("lore").asList()) {
                lore.add(loreElement.toString());
            }
            itemBuilder.setLore(lore);
        }

        if (meta.get("flags").valueType() != ValueType.INVALID) {
            for (Any flagElement : meta.get("flags").asList()) {
                itemBuilder.addItemFlag(ItemFlag.valueOf(flagElement.toString()));
            }
        }

        var additionalData = any.get("durability");

        if (additionalData.valueType() != ValueType.INVALID) {
            itemBuilder.setDurability(additionalData.toInt());
        }

        additionalData = any.get("repairCost");

        if (additionalData.valueType() != ValueType.INVALID) {
            itemBuilder.setRepairCosts(additionalData.toInt());
        }

        return itemBuilder.build();*/
        return null;
    }
}
