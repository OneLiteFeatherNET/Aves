package de.icevizion.aves.decoder;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.spi.Decoder;
import de.icevizion.aves.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ItemStackDecoder implements Decoder {

    @Override
    public Object decode(JsonIterator jsonIterator) throws IOException {
        Any any = jsonIterator.readAny();

        Material material = Material.getMaterial(any.get("material").toString());

        if (material == null) {
            material = Material.STONE;
        }

        ItemBuilder itemBuilder = new ItemBuilder(material);
        itemBuilder.setAmount(any.get("amount").toInt());
        if (any.get("meta") == null) {
            return itemBuilder.build();
        }

        Any meta = any.get("meta");

        if (meta.get("displayName") != null) {
            itemBuilder.setDisplayName(meta.get("displayName").toString());
        }
        if (meta.get("enchantments") != null) {
            List<Any> enchantments = meta.get("enchantments").asList();
            for (Any enchantment : enchantments) {
                Enchantment en = Enchantment.getByKey(NamespacedKey.minecraft(enchantment.get("enchantment").toString()));
                itemBuilder.addEnchantment(en,enchantment.get("level").toInt(), true);
            }
        }
        if (meta.get("lore") != null) {
            List<String> lore = new ArrayList<>();
            for (Any loreElement : meta.get("lore").asList()) {
                lore.add(loreElement.toString());
            }
            itemBuilder.setLore(lore);
        }

        if (meta.get("flags") != null) {
            for (Any flagElement : meta.get("flags").asList()) {
                itemBuilder.addItemFlag(ItemFlag.valueOf(flagElement.toString()));
            }
        }
        return itemBuilder.build();
    }
}
