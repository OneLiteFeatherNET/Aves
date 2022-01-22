package de.icevizion.aves.file.jsoniter;

import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Encoder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class ItemStackEncoder implements Encoder {

    @Override
    public void encode(Object object, JsonStream jsonStream) throws IOException {
        if (object == null) {
            jsonStream.writeNull();
        } else {
          if (object instanceof ItemStack stack) {
              Map<String, Object> map = new HashMap<>();
              map.put("material", stack.getMaterial().name());
              map.put("amount", stack.getAmount());

              var meta = stack.getMeta();

              map.put("damage", meta.getDamage());

              if (meta.getCustomModelData() != 0) {
                  map.put("customModelData", meta.getCustomModelData());
              }

              var metaMap = new HashMap<String, Object>();

              if (meta.getDisplayName() != null) {
                  metaMap.put("displayName", LegacyComponentSerializer.legacySection().serialize(meta.getDisplayName()));
              }

              if (!meta.getEnchantmentMap().isEmpty()) {
                  var enchantments = new ArrayList<>();
                  for (var entry : meta.getEnchantmentMap().entrySet()) {
                      var enchantMap = new HashMap<String, Object>();
                      enchantMap.put("enchantment", entry.getKey().name());
                      enchantMap.put("level", entry.getValue());
                      enchantments.add(enchantMap);
                  }

                  map.put("enchantment", enchantments);
              }

              if (!meta.getLore().isEmpty()) {
                  var loreLines = new ArrayList<>();

                  for (Component component : meta.getLore()) {
                      loreLines.add(LegacyComponentSerializer.legacySection().serialize(component));
                  }

                  map.put("lore", loreLines);
              }

              map.put("meta", metaMap);
              jsonStream.writeVal(HashMap.class, map);
            }
        }
    }
}
