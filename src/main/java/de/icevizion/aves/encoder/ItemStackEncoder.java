package de.icevizion.aves.encoder;

import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Encoder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class ItemStackEncoder implements Encoder {

    @Override
    public void encode(Object object, JsonStream jsonStream) throws IOException {
        if (object == null) {
            jsonStream.writeNull();
            return;
        } else {
            if (object instanceof ItemStack) {
                ItemStack stack = (ItemStack) object;
                Map<String, Object> map = new HashMap<>();
                map.put("material", stack.getType().name());
                map.put("amount", stack.getAmount());
                if (stack.hasItemMeta()) {
                    var metaMap = new HashMap<String, Object>();
                    var meta = stack.getItemMeta();
                    if (meta.hasDisplayName())
                        metaMap.put("displayName", meta.getDisplayName());
                    if (!meta.getItemFlags().isEmpty()) {
                        var flags = new ArrayList<>();
                        for (ItemFlag flag : meta.getItemFlags()) {
                            flags.add(flag.name());
                        }
                        metaMap.put("flags", flags);
                    }
                    if (!meta.getEnchants().isEmpty()) {
                        var enchantments = new ArrayList<>();
                        for (var entry : meta.getEnchants().entrySet()) {
                            var enchantMap = new HashMap<String, Object>();
                            enchantMap.put("enchantment", entry.getKey().getKey().getKey());
                            enchantMap.put("level", entry.getValue());
                            enchantments.add(enchantMap);
                        }
                        metaMap.put("enchantments", enchantments);
                    }
                    if (meta.hasLore()) {
                        metaMap.put("lore", meta.getLore());
                    }

                    map.put("meta", metaMap);
                }
                jsonStream.writeVal(HashMap.class, map);
             }
        }
    }
}
