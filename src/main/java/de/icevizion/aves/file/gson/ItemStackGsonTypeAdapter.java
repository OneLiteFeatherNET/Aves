package de.icevizion.aves.file.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ItemStackTypeAdapter allows serializing and deserialize from {@link ItemStack} into a valid json object or from a json object into {@link ItemStack}
 * The adapter use the {@link GsonBuilder} to register a custom adapter with the {@link GsonBuilder#registerTypeAdapter(Type, Object)} method
 * with the parameters {@link ItemStack} class and an instance of the {@link ItemStackGsonTypeAdapter}
 */
public class ItemStackGsonTypeAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    private static final String DISPLAY_NAME = "displayName";

    private static final String ENCHANTMENTS = "enchantments";

    @Override
    public JsonElement serialize(@NotNull ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("material", itemStack.material().namespace().namespace());
        object.addProperty("amount", itemStack.amount());

        JsonObject metaObject = new JsonObject();
        ItemMeta meta = itemStack.meta();
        if (meta.getDisplayName() != null)
            metaObject.addProperty(DISPLAY_NAME, PlainTextComponentSerializer.plainText().serialize(meta.getDisplayName()));
        if (!meta.getEnchantmentMap().isEmpty()) {
            JsonArray enchantsArray = new JsonArray();
            for (var enchantEntry : meta.getEnchantmentMap().entrySet()) {
                JsonObject enchantmentObject = new JsonObject();
                enchantmentObject.addProperty("enchantment", enchantEntry.getKey().name());
                enchantmentObject.addProperty("level", enchantEntry.getValue());
                enchantsArray.add(enchantmentObject);
            }
            metaObject.add(ENCHANTMENTS, enchantsArray);
        }
        object.add("meta", metaObject);

        return object;
    }

    @Override
    public ItemStack deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        Material material = Material.fromNamespaceId(object.get("material").getAsString());
        if (material == null)
            material = Material.STONE;

        var itemBuilder = ItemStack.builder(material);
        itemBuilder.amount(object.get("amount").getAsInt());

        if (!object.has("meta")) {
            return itemBuilder.build();
        }

        JsonObject metaObject = object.getAsJsonObject("meta");

        if (metaObject.has(DISPLAY_NAME))
            itemBuilder.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(metaObject.get(DISPLAY_NAME).getAsString()));
        if (metaObject.has(ENCHANTMENTS)) {
            JsonArray enchantsArray = metaObject.getAsJsonArray(ENCHANTMENTS);

            Map<Enchantment, Short> enchantments = new HashMap<>();

            for (JsonElement enchantElement : enchantsArray) {
                var level = ((JsonObject)enchantElement).get("level").getAsShort();
                var nameSpace = ((JsonObject)enchantElement).get("enchantment").getAsString();
                enchantments.putIfAbsent(Enchantment.fromNamespaceId(nameSpace), level);
            }
            itemBuilder.meta(itemMetaBuilder -> itemMetaBuilder.enchantments(enchantments));
        }
        if (metaObject.has("lore")) {
            JsonArray loreArray = metaObject.getAsJsonArray("lore");
            List<Component> lore = new ArrayList<>();
            for (JsonElement element : loreArray)
                lore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(element.getAsString()));
            itemBuilder.lore(lore);
        }
        if (metaObject.has("flags")) {
            JsonArray flagArray = metaObject.getAsJsonArray("flags");
            for (JsonElement element : flagArray)
                itemBuilder.meta(itemMetaBuilder -> itemMetaBuilder.hideFlag(ItemHideFlag.valueOf(element.getAsString())));
        }

        return itemBuilder.build();
    }
}

