package net.theevilreaper.aves.file.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import static net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText;

/**
 * The {@link ItemStackSerializerHelper} is a internal interface which contains some methods to serialize or deserialize data from or to an {@link ItemStack}.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
sealed interface ItemStackSerializerHelper permits ItemStackGsonTypeAdapter {

    String DISPLAY_NAME = "displayName";
    String LORE_KEY = "lore";
    String ENCHANTMENTS = "enchantments";

    /**
     * Serializes the custom name from the {@link ItemStack} into the given {@link JsonObject}.
     *
     * @param stack  the stack to serialize
     * @param object the object to serialize the data
     */
    default void serializeCustomName(@NotNull ItemStack stack, @NotNull JsonObject object) {
        if (!stack.has(DataComponents.CUSTOM_NAME)) return;
        final Component itemName = stack.get(DataComponents.CUSTOM_NAME, Component.empty());
        final String displayName = plainText().serialize(itemName);
        object.addProperty(DISPLAY_NAME, displayName);
    }

    /**
     * Deserializes the custom name from the {@link JsonObject} into the {@link ItemStack.Builder}.
     *
     * @param stack      the builder to set the custom name
     * @param jsonObject the object to deserialize the data
     */
    default void serializeLore(@NotNull ItemStack stack, @NotNull JsonObject jsonObject) {
        if (!stack.has(DataComponents.LORE)) return;
        final List<Component> loreLines = stack.get(DataComponents.LORE);

        if (loreLines != null && !loreLines.isEmpty()) {
            JsonArray loreArray = new JsonArray();
            for (Component loreLine : loreLines) {
                loreArray.add(plainText().serialize(loreLine));
            }
            jsonObject.add(LORE_KEY, loreArray);
        }
    }

    /**
     * Deserializes the lore from the {@link JsonObject} into the {@link ItemStack.Builder}.
     *
     * @param builder the builder to set the lore
     * @param object  the object to deserialize the data
     * @return the builder with the lore set
     */
    default @NotNull ItemStack.Builder deserializeLore(@NotNull ItemStack.Builder builder, @NotNull JsonObject object) {
        if (!object.has(LORE_KEY)) return builder;

        JsonArray loreArray = object.getAsJsonArray(LORE_KEY);
        List<Component> lore = new ArrayList<>();
        for (JsonElement element : loreArray) {
            lore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(element.getAsString()));
        }
        return builder.lore(lore);
    }

    /**
     * Serializes the enchantments from the {@link ItemStack} into the given {@link JsonObject}.
     *
     * @param stack  the stack to serialize
     * @param object the object to serialize the data
     */
    default void serializeEnchantments(@NotNull ItemStack stack, @NotNull JsonObject object) {
        if (!stack.has(DataComponents.ENCHANTMENTS)) return;
        JsonArray enchantsArray = new JsonArray();
        final EnchantmentList enchantmentList = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantmentList != null && !enchantmentList.enchantments().isEmpty()) {
            Set<Map.Entry<RegistryKey<Enchantment>, Integer>> entries = enchantmentList.enchantments().entrySet();
            for (Map.Entry<RegistryKey<Enchantment>, Integer> entry : entries) {
                JsonObject enchantmentObject = new JsonObject();
                enchantmentObject.addProperty("enchantment", entry.getKey().name());
                enchantmentObject.addProperty("level", entry.getValue());
                enchantsArray.add(enchantmentObject);
            }
            object.add(ENCHANTMENTS, enchantsArray);
        }
    }

    /**
     * Deserializes the enchantments from the {@link JsonObject} into the {@link ItemStack.Builder}.
     *
     * @param builder the builder to set the enchantments
     * @param object  the object to deserialize the data
     * @return the builder with the enchantments set
     */
    default @NotNull ItemStack.Builder deserializeEnchantments(@NotNull ItemStack.Builder builder, @NotNull JsonObject object) {
        if (!object.has(ENCHANTMENTS)) return builder;
        JsonArray enchantsArray = object.getAsJsonArray(ENCHANTMENTS);
        Map<RegistryKey<Enchantment>, Integer> enchantments = new HashMap<>();
        for (JsonElement enchantElement : enchantsArray) {
            final JsonObject enchantObject = (JsonObject) enchantElement;
            String nameSpace = enchantObject.get("enchantment").getAsString();
            var level = enchantObject.get("level").getAsInt();
            DynamicRegistry<Enchantment> enchantmentRegistry = MinecraftServer.getEnchantmentRegistry();
            Enchantment rawEnchantment = enchantmentRegistry.get(Key.key(nameSpace));
            if (rawEnchantment == null) continue;
            RegistryKey<Enchantment> enchantment = enchantmentRegistry.getKey(rawEnchantment);
            enchantments.putIfAbsent(enchantment, level);
        }
        EnchantmentList enchantmentList = new EnchantmentList(enchantments);
        return builder.set(DataComponents.ENCHANTMENTS, enchantmentList);
    }
}
