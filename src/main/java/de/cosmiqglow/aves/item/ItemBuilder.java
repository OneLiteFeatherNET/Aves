package de.cosmiqglow.aves.item;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemBuilder {

    protected final ItemStack stack;

    public ItemBuilder(Material material) {
        Objects.requireNonNull(material, "Material can not be null");
        this.stack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "ItemStack can not be null");
        this.stack = itemStack;
    }

    public ItemBuilder setAmount(final int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchantment(final Enchantment enchantment, final int level) {
        this.stack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(final Enchantment enchantment, final int level) {
        this.stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder setDisplayName(final String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = getItemMeta();
        meta.setUnbreakable(unbreakable);
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlag(final ItemFlag flag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(final String... lore) {
        ItemMeta meta = getItemMeta();
        List<String> currentLore = meta.getLore();
        if (currentLore == null) currentLore = new ArrayList<>();
        currentLore.addAll(Arrays.asList(lore));
        meta.setLore(currentLore);
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(final List<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setBlockState(final BlockState blockState) {
        BlockStateMeta meta = (BlockStateMeta) getItemMeta();
        meta.setBlockState(blockState);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setRepairCosts(final int repairCosts) {
        Preconditions.checkArgument(repairCosts < 0, "The costs can not be negative");
        Repairable meta = (Repairable) getItemMeta();
        meta.setRepairCost(repairCosts);
        return this;
    }

    private ItemBuilder setItemMeta(final ItemMeta meta) {
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return this.stack;
    }

    protected ItemMeta getItemMeta() {
        return this.stack.getItemMeta();
    }
}