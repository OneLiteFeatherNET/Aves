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
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.*;

/**
 * The ItemStackTypeAdapter allows serializing and deserialize from {@link ItemStack} into a valid json object or from a json object into {@link ItemStack}
 * The adapter use the {@link GsonBuilder} to register a custom adapter with the {@link GsonBuilder#registerTypeAdapter(Type, Object)} method
 * with the parameters {@link ItemStack} class and an instance of the {@link ItemStackGsonTypeAdapter}
 */
@SuppressWarnings("java:S3252")
public class ItemStackGsonTypeAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    private static final String DISPLAY_NAME = "displayName";
    private static final String ENCHANTMENTS = "enchantments";
    private static final String MATERIAL = "material";
    private static final String CUSTOM_MODEL_DATA = "customModelData";

    @Override
    public JsonElement serialize(@NotNull ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty(MATERIAL, itemStack.material().name());
        object.addProperty("amount", itemStack.amount());

        JsonObject metaObject = new JsonObject();

        if (itemStack.has(ItemComponent.CUSTOM_NAME)) {
            final Component itemName = itemStack.get(ItemComponent.CUSTOM_NAME, Component.empty());
            final String displayName = PlainTextComponentSerializer.plainText().serialize(itemName);
            metaObject.addProperty(DISPLAY_NAME, displayName);
        }

        if (itemStack.has(ItemComponent.LORE)) {
            final List<Component> loreLines = itemStack.get(ItemComponent.LORE);

            if (loreLines != null && !loreLines.isEmpty()) {
                JsonArray loreArray = new JsonArray();
                for (Component loreLine : loreLines) {
                    loreArray.add(PlainTextComponentSerializer.plainText().serialize(loreLine));
                }
                metaObject.add("lore", loreArray);
            }
        }

        if (itemStack.has(ItemComponent.HIDE_TOOLTIP)) {
            metaObject.addProperty("hideTooltip", "true");
        }

        if (itemStack.has(ItemComponent.HIDE_ADDITIONAL_TOOLTIP)) {
            metaObject.addProperty("hideAdditionalTooltip", "true");
        }

        if (itemStack.has(ItemComponent.CUSTOM_MODEL_DATA)) {
            metaObject.addProperty(CUSTOM_MODEL_DATA, itemStack.get(ItemComponent.CUSTOM_MODEL_DATA));
        }

        if (itemStack.has(ItemComponent.ENCHANTMENTS)) {
            JsonArray enchantsArray = new JsonArray();
            final EnchantmentList enchantmentList = itemStack.get(ItemComponent.ENCHANTMENTS);
            if (enchantmentList != null && !enchantmentList.enchantments().isEmpty()) {
                Set<Map.Entry<DynamicRegistry.Key<Enchantment>, Integer>> entries = enchantmentList.enchantments().entrySet();
                for (Map.Entry<DynamicRegistry.Key<Enchantment>, Integer> keyIntegerEntry : entries) {
                    JsonObject enchantmentObject = new JsonObject();
                    enchantmentObject.addProperty("enchantment", keyIntegerEntry.getKey().name());
                    enchantmentObject.addProperty("level", keyIntegerEntry.getValue());
                    enchantsArray.add(enchantmentObject);
                }
                metaObject.add(ENCHANTMENTS, enchantsArray);
            }
        }

        object.add("meta", metaObject);
        return object;
    }

    @Override
    public ItemStack deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        Material material = Material.STONE;

        if (object.has(MATERIAL)) {
            var materialString = object.get(MATERIAL).getAsString();
            var fetchedMaterial = Material.fromNamespaceId(materialString);
            material = fetchedMaterial == null ? Material.STONE : fetchedMaterial;
        }

        ItemStack.Builder itemBuilder = ItemStack.builder(material);
        itemBuilder.amount(object.get("amount").getAsInt());

        if (!object.has("meta")) {
            return itemBuilder.build();
        }

        JsonObject metaObject = object.getAsJsonObject("meta");

        if (metaObject.has(DISPLAY_NAME)) {
            String rawDisplayName = metaObject.get(DISPLAY_NAME).getAsString();
            itemBuilder.customName(LegacyComponentSerializer.legacyAmpersand().deserialize(rawDisplayName));
        }

        if (metaObject.has("lore")) {
            JsonArray loreArray = metaObject.getAsJsonArray("lore");
            List<Component> lore = new ArrayList<>();
            for (JsonElement element : loreArray) {
                lore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(element.getAsString()));
            }
            itemBuilder.lore(lore);
        }

        if (metaObject.has("hideTooltip")) {
            itemBuilder.set(ItemComponent.HIDE_TOOLTIP);
        }

        if (metaObject.has("hideAdditionalTooltip")) {
            itemBuilder.set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP);
        }

        if (metaObject.has(CUSTOM_MODEL_DATA)) {
            int customModelData = metaObject.get(CUSTOM_MODEL_DATA).getAsInt();
            itemBuilder.set(ItemComponent.CUSTOM_MODEL_DATA, customModelData);
        }

        if (metaObject.has(ENCHANTMENTS)) {
            JsonArray enchantsArray = metaObject.getAsJsonArray(ENCHANTMENTS);
            Map<DynamicRegistry.Key<Enchantment>, Integer> enchantments = new HashMap<>();
            for (JsonElement enchantElement : enchantsArray) {
                final JsonObject enchantObject = (JsonObject) enchantElement;
                String nameSpace = enchantObject.get("enchantment").getAsString();
                var level = enchantObject.get("level").getAsInt();
                DynamicRegistry<Enchantment> enchantmentRegistry = MinecraftServer.getEnchantmentRegistry();
                Enchantment rawEnchantment = enchantmentRegistry.get(NamespaceID.from(nameSpace));
                if (rawEnchantment == null) continue;
                DynamicRegistry.Key<Enchantment> enchantment = enchantmentRegistry.getKey(rawEnchantment);
                enchantments.putIfAbsent(enchantment, level);
            }
            EnchantmentList enchantmentList = new EnchantmentList(enchantments);
            itemBuilder.set(ItemComponent.ENCHANTMENTS, enchantmentList);
        }
        return itemBuilder.build();
    }
}

