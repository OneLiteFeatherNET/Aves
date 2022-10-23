package de.icevizion.aves.file.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemStackGsonTypeAdapterTest {

    final String STACK_WITH_AMOUNT = """
           {"material":"minecraft:allium","amount":1,"meta":{}}
            """.trim();

    final String STACK_WITH_DISPLAYNAME = """
            {"material":"minecraft:allium","amount":1,"meta":{"displayName":"Test"}}
            """.trim();

    final String STACK_WITH_ENCHANTMENTS = """
            {"material":"minecraft:diamond","amount":1,"meta":{"enchantments":[{"enchantment":"minecraft:channeling","level":1}]}}
            """.trim();

    final String STACK_WITH_LORE = """
            {"material":"minecraft:light","amount":1,"meta":{"lore":["Test1","Test2"]}}
            """.trim();

    Gson gson;

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
                .displayName(Component.text("Test")).build();
        var json = gson.toJson(stack, ItemStack.class);
        assertEquals(STACK_WITH_DISPLAYNAME, json);
    }

    @Test
    void testItemWriteWithEnchantmentsWrite() {
        var stack = ItemStack.builder(Material.DIAMOND)
                .meta(builder -> builder.enchantment(Enchantment.CHANNELING, (short)1)).build();
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
    void testItemReadWithoutMeta() {
        var originalStack = ItemStack.builder(Material.ALLIUM).amount(1).build();
        var stack = gson.fromJson(STACK_WITH_AMOUNT, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWithDisplayName() {
        var originalStack = ItemStack.builder(Material.ALLIUM).amount(1)
                .displayName(Component.text("Test")).build();
        var stack = gson.fromJson(STACK_WITH_DISPLAYNAME, ItemStack.class);
        assertEquals(originalStack, stack);
    }

    @Test
    void testItemReadWitEnchantments() {
        var originalStack = ItemStack.builder(Material.DIAMOND)
                .meta(builder -> builder.enchantment(Enchantment.CHANNELING, (short)1)).build();
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