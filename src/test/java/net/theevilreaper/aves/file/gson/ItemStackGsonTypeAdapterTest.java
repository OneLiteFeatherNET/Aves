package net.theevilreaper.aves.file.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.CustomModelData;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class ItemStackGsonTypeAdapterTest {

    static final String STACK_WITHOUT_MATERIAL_AND_META = """
            {"amount":1}
            """.trim();

    static final String STACK_WITH_AMOUNT = """
            {"material":"minecraft:allium","amount":1,"meta":{}}
            """.trim();

    static final String STACK_WITH_DISPLAYNAME = """
            {"material":"minecraft:allium","amount":1,"meta":{"displayName":"Test"}}
            """.trim();

    static final String STACK_WITH_ENCHANTMENTS = """
            {"material":"minecraft:diamond","amount":1,"meta":{"enchantments":[{"enchantment":"minecraft:channeling","level":1}]}}
            """.trim();

    static final String STACK_WITH_LORE = """
            {"material":"minecraft:light","amount":1,"meta":{"lore":["Test1","Test2"]}}
            """.trim();

    private static Gson gson;

    @BeforeAll
    static void init() {
        gson = new GsonBuilder().registerTypeAdapter(TypeToken.get(ItemStack.class).getType(), new ItemStackGsonTypeAdapter()).create();
    }

    @AfterAll
    static void destroy() {
        gson = null;
    }

    @Test
    void testItemStackWrite() {
        ItemStack stack = ItemStack.builder(Material.ALLIUM).amount(1).build();
        String jsonString = gson.toJson(stack, ItemStack.class);
        assertEquals(STACK_WITH_AMOUNT, jsonString);
    }

    @Test
    void testItemStackWriteWithCustomData() {
        ItemStack stack = ItemStack.builder(Material.DIAMOND)
                .amount(1)
                .customModelData(List.of(1f), List.of(true, false), List.of(), List.of())
                .build();
        String json = gson.toJson(stack, ItemStack.class);

        assertTrue(json.contains("customModelData"));

        ItemStack decodedStack = gson.fromJson(json, ItemStack.class);

        assertNotNull(decodedStack);
        assertEquals(Material.DIAMOND, decodedStack.material());

        CustomModelData data = decodedStack.get(ItemComponent.CUSTOM_MODEL_DATA);
        assertNotNull(data);

        assertEquals(List.of(1f), data.floats());
        assertEquals(List.of(true, false), data.flags());
        assertTrue(data.strings().isEmpty());
        assertTrue(data.colors().isEmpty());
    }

    @Test
    void testItemWriteWithDisplayNameWrite() {
        ItemStack stack = ItemStack.builder(Material.ALLIUM).amount(1)
                .customName(Component.text("Test")).build();
        String json = gson.toJson(stack, ItemStack.class);
        assertEquals(STACK_WITH_DISPLAYNAME, json);
    }

    @Test
    void testItemWriteWithEnchantmentsWrite() {
        var enchantmentList = new EnchantmentList(Enchantment.CHANNELING, 1);
        ItemStack stack = ItemStack.builder(Material.DIAMOND)
                .set(ItemComponent.ENCHANTMENTS, enchantmentList)
                .build();
        String json = gson.toJson(stack, ItemStack.class);
        assertEquals(STACK_WITH_ENCHANTMENTS, json);
    }

    @Test
    void testItemWithLoreWrite() {
        ItemStack stack = ItemStack.builder(Material.LIGHT)
                .lore(List.of(Component.text("Test1"), Component.text("Test2"))).build();
        String json = gson.toJson(stack, ItemStack.class);
        assertEquals(STACK_WITH_LORE, json);
    }

    @Test
    void testItemReadWithoutMaterialAndMeta() {
        ItemStack originalStack = ItemStack.builder(Material.STONE).amount(1).build();
        ItemStack stack = gson.fromJson(STACK_WITHOUT_MATERIAL_AND_META, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWithoutMeta() {
        ItemStack originalStack = ItemStack.builder(Material.ALLIUM).amount(1).build();
        ItemStack stack = gson.fromJson(STACK_WITH_AMOUNT, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWithDisplayName() {
        ItemStack originalStack = ItemStack.builder(Material.ALLIUM).amount(1)
                .customName(Component.text("Test")).build();
        ItemStack stack = gson.fromJson(STACK_WITH_DISPLAYNAME, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWitEnchantments(Env env) {
        var enchantmentList = new EnchantmentList(Enchantment.CHANNELING, 1);
        ItemStack originalStack = ItemStack.builder(Material.DIAMOND)
                .set(ItemComponent.ENCHANTMENTS, enchantmentList)
                .build();
        ItemStack stack = gson.fromJson(STACK_WITH_ENCHANTMENTS, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWithLore() {
        ItemStack originalStack = ItemStack.builder(Material.LIGHT)
                .lore(List.of(Component.text("Test1"), Component.text("Test2"))).build();
        ItemStack stack = gson.fromJson(STACK_WITH_LORE, ItemStack.class);
        assertEquals(originalStack, stack);
    }
}