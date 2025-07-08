package net.theevilreaper.aves.file.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.CustomModelData;
import net.minestom.server.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * The ItemStackTypeAdapter allows serializing and deserialize from {@link ItemStack} into a valid json object or from a json object into {@link ItemStack}
 * The adapter use the {@link GsonBuilder} to register a custom adapter with the {@link GsonBuilder#registerTypeAdapter(Type, Object)} method
 * with the parameters {@link ItemStack} class and an instance of the {@link ItemStackGsonTypeAdapter}
 */
@SuppressWarnings("java:S3252")
public non-sealed class ItemStackGsonTypeAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack>, ItemStackSerializerHelper {

    private static final String MATERIAL = "material";
    private static final String CUSTOM_MODEL_DATA = "customModelData";

    @Override
    public JsonElement serialize(@NotNull ItemStack itemStack, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(MATERIAL, itemStack.material().name());
        object.addProperty("amount", itemStack.amount());

        JsonObject metaObject = new JsonObject();
        serializeCustomName(itemStack, metaObject);
        serializeLore(itemStack, metaObject);


        if (itemStack.has(DataComponents.TOOLTIP_DISPLAY)) {
            TooltipDisplay tooltipDisplay = itemStack.get(DataComponents.TOOLTIP_DISPLAY);
            if (tooltipDisplay.hideTooltip()) {
                metaObject.addProperty("hideTooltip", true);
            }
        }


        if (itemStack.has(DataComponents.CUSTOM_MODEL_DATA)) {
            CustomModelData modelData = itemStack.get(DataComponents.CUSTOM_MODEL_DATA);
            JsonElement customData = context.serialize(modelData);
            metaObject.add(CUSTOM_MODEL_DATA, customData);
        }

        serializeEnchantments(itemStack, metaObject);
        object.add("meta", metaObject);
        return object;
    }

    @Override
    public ItemStack deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        Material material = Material.STONE;

        if (object.has(MATERIAL)) {
            String materialString = object.get(MATERIAL).getAsString();
            Material fetchedMaterial = Material.fromKey(materialString);
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

        itemBuilder = deserializeLore(itemBuilder, metaObject);

        if (metaObject.has("hideTooltip")) {
            itemBuilder.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(true, null));
        }

        if (metaObject.has(CUSTOM_MODEL_DATA)) {
            JsonElement customModelElement = metaObject.get(CUSTOM_MODEL_DATA);
            CustomModelData customModelData = context.deserialize(customModelElement, CustomModelData.class);
            itemBuilder.set(DataComponents.CUSTOM_MODEL_DATA, customModelData);
        }

        itemBuilder = deserializeEnchantments(itemBuilder, metaObject);
        return itemBuilder.build();
    }
}

