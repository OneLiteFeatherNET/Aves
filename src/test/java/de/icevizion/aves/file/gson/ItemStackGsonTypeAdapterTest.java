package de.icevizion.aves.file.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    private Gson gson;

    @BeforeAll
    void init() {
        this.gson = new GsonBuilder().registerTypeAdapter(TypeToken.get(ItemStack.class).getType(), new ItemStackGsonTypeAdapter()).create();
    }

    @Test
    void testItemStackWrite() {
        var stack = ItemStack.builder(Material.ALLIUM).amount(1).build();
        var jsonString = gson.toJson(stack, ItemStack.class);
        assertEquals(STACK_WITH_AMOUNT, jsonString);
    }

    @Test
    void testItemWriteWithDisplayNameWrite() {
        var stack = ItemStack.builder(Material.ALLIUM).amount(1)
                .customName(Component.text("Test")).build();
        var json = gson.toJson(stack, ItemStack.class);
        assertEquals(STACK_WITH_DISPLAYNAME, json);
    }

    @Test
    void testItemWriteWithEnchantmentsWrite() {
        var enchantmentList = new EnchantmentList(Enchantment.CHANNELING, 1);
        var stack = ItemStack.builder(Material.DIAMOND)
                .set(ItemComponent.ENCHANTMENTS, enchantmentList)
                .build();
        var json = gson.toJson(stack, ItemStack.class);
        assertEquals(STACK_WITH_ENCHANTMENTS, json);
    }

    @Test
    void testItemWithLoreWrite() {
        var stack = ItemStack.builder(Material.LIGHT)
                .lore(List.of(Component.text("Test1"), Component.text("Test2"))).build();
        var json = gson.toJson(stack, ItemStack.class);
        assertEquals(STACK_WITH_LORE, json);
    }

    @Test
    void testItemReadWithoutMaterialAndMeta() {
        var originalStack = ItemStack.builder(Material.STONE).amount(1).build();
        var stack = gson.fromJson(STACK_WITHOUT_MATERIAL_AND_META, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWithoutMeta() {
        var originalStack = ItemStack.builder(Material.ALLIUM).amount(1).build();
        var stack = gson.fromJson(STACK_WITH_AMOUNT, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWithDisplayName() {
        var originalStack = ItemStack.builder(Material.ALLIUM).amount(1)
                .customName(Component.text("Test")).build();
        var stack = gson.fromJson(STACK_WITH_DISPLAYNAME, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWitEnchantments(Env env) {
        var enchantmentList = new EnchantmentList(Enchantment.CHANNELING, 1);
        var originalStack = ItemStack.builder(Material.DIAMOND)
                .set(ItemComponent.ENCHANTMENTS, enchantmentList)
                .build();
        var stack = gson.fromJson(STACK_WITH_ENCHANTMENTS, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWithLore() {
        var originalStack = ItemStack.builder(Material.LIGHT)
                .lore(List.of(Component.text("Test1"), Component.text("Test2"))).build();
        var stack = gson.fromJson(STACK_WITH_LORE, ItemStack.class);
        assertEquals(originalStack, stack);
    }
}