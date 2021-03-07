package de.icevizion.aves.encoder;

import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Encoder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.Map;

public final class ItemStackEncoder implements Encoder {
    @Override
    public void encode(Object obj, JsonStream stream) throws IOException {
        System.out.println(obj.toString());
        if (obj instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) obj;
            Any any = Any.wrapNull();
            any.get("material").set(itemStack.getType().name());
            any.get("amount").set(itemStack.getAmount());
            if (itemStack.hasItemMeta()) {
                Any meta = any.get("meta");
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.hasDisplayName())
                    meta.get("displayName").set(itemMeta.getDisplayName());
                if (itemMeta.hasEnchants()) {
                    for (Map.Entry<Enchantment, Integer> entry : itemMeta.getEnchants().entrySet()) {
                        Any enchantmentObject = Any.wrapNull();
                        enchantmentObject.get("enchantment").set(entry.getKey().getName());
                        enchantmentObject.get("level").set(entry.getValue());
                        meta.get("enchantments").asList().add(enchantmentObject);
                    }
                }
                meta.bindTo(any);
            }
            any.writeTo(stream);
        }
    }
}
