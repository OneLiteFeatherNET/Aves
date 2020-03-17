package de.icevizion.aves.item;

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

    protected ItemStack stack;

    public ItemBuilder(Material material) {
        Objects.requireNonNull(material, "Material can not be null");
        this.stack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "ItemStack can not be null");
        this.stack = itemStack;
    }

    /**
     * Set the number of items from this stack
     * @param amount New amount of items in this stack
     * @return
     */

    public ItemBuilder setAmount(final int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    /**
     * Adds the specified Enchantment to this item stack
     * @param enchantment The enchantment to be add
     * @param level The level of the enchantment
     * @return
     */

    public ItemBuilder addEnchantment(final Enchantment enchantment, final int level) {
        this.stack.addEnchantment(enchantment, level);
        return this;
    }

    /**
     * Adds the specified Enchantment to this item stack in an unsafe manner
     * @param enchantment The enchantment to be add
     * @param level The level of the enchantment
     * @return
     */

    public ItemBuilder addUnsafeEnchantment(final Enchantment enchantment, final int level) {
        this.stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Sets the displayname
     * @param name The name to set
     * @return
     */

    public ItemBuilder setDisplayName(final String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     * @param unbreakable true if set unbreakable
     * @return
     */

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client
     * @param flag The hideflags which shouldn't be rendered
     * @return
     */

    public ItemBuilder addItemFlag(final ItemFlag flag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the lore for this item. Removes lore when given null
     * @param lore The lore that will be set
     * @return
     */

    public ItemBuilder addLore(final String... lore) {
        ItemMeta meta = getItemMeta();
        List<String> currentLore = meta.getLore();
        if (currentLore == null) currentLore = new ArrayList<>();
        currentLore.addAll(Arrays.asList(lore));
        meta.setLore(currentLore);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the lore for this item. Removes lore when given null
     * @param lore The lore that will be set
     * @return
     */

    public ItemBuilder addLore(final List<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Attaches a copy of the passed block state to the item
     * @param blockState the block state to attach to the block
     * @return
     */

    public ItemBuilder setBlockState(final BlockState blockState) {
        BlockStateMeta meta = (BlockStateMeta) getItemMeta();
        meta.setBlockState(blockState);
        setItemMeta(meta);
        return this;
    }

    /**
     * Sets the repair penalty
     * @param repairCosts repair penalty
     * @return
     */

    public ItemBuilder setRepairCosts(final int repairCosts) {
        Preconditions.checkArgument(repairCosts < 0, "The costs can not be negative");
        Repairable meta = (Repairable) getItemMeta();
        meta.setRepairCost(repairCosts);
        return this;
    }

    /**
     * Set the ItemMeta of this ItemStack
     * @param meta  new ItemMeta, or null to indicate meta data be cleared
     * @return
     */

    private ItemBuilder setItemMeta(final ItemMeta meta) {
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Builds the stack
     * @return The stack
     */

    public ItemStack build() {
        return this.stack;
    }

    /**
     * Returns from a item the itemmeta
     * @return The itemmeta
     */

    protected ItemMeta getItemMeta() {
        return this.stack.getItemMeta();
    }
}