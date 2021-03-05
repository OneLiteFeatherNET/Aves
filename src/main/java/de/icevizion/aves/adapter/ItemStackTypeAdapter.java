package de.icevizion.aves.adapter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.icevizion.aves.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The ItemStackTypeAdapter allows to serialize and deserialize from {@link ItemStack} into a valid json object or from a json object into {@link ItemStack}
 * The adapter use the {@link GsonBuilder} to register a custom adapter with the {@link GsonBuilder#registerTypeAdapter(Type, Object)} method
 * with the parameters {@link ItemStack} class and a instance of the {@link ItemStackTypeAdapter}
 */
@Deprecated(forRemoval = true)
public class ItemStackTypeAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("material", itemStack.getType().name());
        object.addProperty("amount", itemStack.getAmount());
        if (itemStack.hasItemMeta()) {
            JsonObject metaObject = new JsonObject();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta.hasDisplayName())
                metaObject.addProperty("displayName", meta.getDisplayName());
            if (meta.hasEnchants()) {
                JsonArray enchantsArray = new JsonArray();
                for (Map.Entry<Enchantment, Integer> enchantEntry : meta.getEnchants().entrySet()) {
                    JsonObject enchantmentObject = new JsonObject();
                    enchantmentObject.addProperty("enchantment", enchantEntry.getKey().getName());
                    enchantmentObject.addProperty("level", enchantEntry.getValue());
                    enchantsArray.add(enchantmentObject);
                }
                metaObject.add("enchantments", enchantsArray);
            }
            object.add("meta", metaObject);
        }

        return object;
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        Material material = Material.getMaterial(object.get("material").getAsString());
        if (material == null)
            material = Material.STONE;

        ItemBuilder itemBuilder = new ItemBuilder(material);
        itemBuilder.setAmount(object.get("amount").getAsInt());

        if (!object.has("meta")) {
            return itemBuilder.build();
        }

        JsonObject metaObject = object.getAsJsonObject("meta");

        if (metaObject.has("displayName"))
            itemBuilder.setDisplayName(metaObject.get("displayName").getAsString());
        if (metaObject.has("enchantments")) {
            JsonArray enchantsArray = metaObject.getAsJsonArray("enchantments");
            for (JsonElement enchantElement : enchantsArray) {
                itemBuilder.addUnsafeEnchantment(
                        Enchantment.getByName(((JsonObject) enchantElement).get("enchantment").getAsString()),
                        ((JsonObject) enchantElement).get("level").getAsInt());
            }
        }
        if (metaObject.has("lore")) {
            JsonArray loreArray = metaObject.getAsJsonArray("lore");
            List<String> lore = new ArrayList<>();
            for (JsonElement element : loreArray)
                lore.add(element.getAsString());
            itemBuilder.setLore(lore);
        }
        if (metaObject.has("flags")) {
            JsonArray flagArray = metaObject.getAsJsonArray("flags");
            for (JsonElement element : flagArray)
                itemBuilder.addItemFlag(ItemFlag.valueOf(element.getAsString()));
        }

        return itemBuilder.build();
    }
}

