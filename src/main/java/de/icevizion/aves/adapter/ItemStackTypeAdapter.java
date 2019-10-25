package de.icevizion.aves.adapter;

/**
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0
 * @since 20/10/2019 10:40
 */


import com.google.gson.*;
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
public class ItemStackTypeAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.add("material", new JsonPrimitive(itemStack.getType().name()));
        object.add("amount", new JsonPrimitive(itemStack.getAmount()));
        object.add("durability", new JsonPrimitive(itemStack.getDurability()));
        if (itemStack.hasItemMeta()) {
            JsonObject metaObject = new JsonObject();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta.hasDisplayName())
                metaObject.add("displayName", new JsonPrimitive(meta.getDisplayName()));
            if (meta.hasEnchants()) {
                JsonArray enchantsArray = new JsonArray();
                for (Map.Entry<Enchantment, Integer> enchantEntry : meta.getEnchants().entrySet()) {
                    JsonObject enchantmentObject = new JsonObject();
                    enchantmentObject.add("enchantment", new JsonPrimitive(enchantEntry.getKey().getName()));
                    enchantmentObject.add("level", new JsonPrimitive(enchantEntry.getValue()));
                    enchantsArray.add(enchantmentObject);
                }
                metaObject.add("enchantments", enchantsArray);
            }
            if (meta.hasLore()) {
                JsonArray loreArray = new JsonArray();
                for (String lore : meta.getLore())
                    loreArray.add(lore);
                metaObject.add("lore", loreArray);
            }
            if (meta.getItemFlags() != null && meta.getItemFlags().size() > 0) {
                JsonArray flagArray = new JsonArray();
                for (ItemFlag itemFlag : meta.getItemFlags())
                    flagArray.add(itemFlag.name());
                metaObject.add("flags", flagArray);
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
        int amount = object.get("amount").getAsInt();
        short durability = object.get("durability").getAsShort();
        ItemStack itemStack = new ItemStack(material, amount);
        itemStack.setDurability(durability);

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (object.has("meta")) {
            JsonObject metaObject = object.getAsJsonObject("meta");
            if (metaObject.has("displayName"))
                itemMeta.setDisplayName(metaObject.get("displayName").getAsString());
            if (metaObject.has("enchantments")) {
                JsonArray enchantsArray = metaObject.getAsJsonArray("enchantments");
                for (JsonElement enchantElement : enchantsArray) {
                    itemStack.addUnsafeEnchantment(
                            Enchantment.getByName(((JsonObject) enchantElement).get("enchantment").getAsString()),
                            ((JsonObject) enchantElement).get("level").getAsInt());
                }
            }
            if (metaObject.has("lore")) {
                JsonArray loreArray = metaObject.getAsJsonArray("lore");
                List<String> lore = new ArrayList<>();
                for (JsonElement element : loreArray)
                    lore.add(element.getAsString());
                itemMeta.setLore(lore);
            }
            if (metaObject.has("flags")) {
                JsonArray flagArray = metaObject.getAsJsonArray("flags");
                for (JsonElement element : flagArray)
                    itemMeta.addItemFlags(ItemFlag.valueOf(element.getAsString()));
            }
        }

        return itemStack;
    }
}

