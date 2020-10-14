package de.icevizion.aves.item;

import net.titan.cloudcore.i18n.Translator;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemBuilder {

    protected ItemStack stack;
    protected ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        Objects.requireNonNull(material, "Material can not be null");
        this.stack = new ItemStack(material);
        this.itemMeta = stack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "ItemStack can not be null");
        this.stack = itemStack;
        this.itemMeta = stack.getItemMeta();
    }

    /**
     * Set the number of items from this stack
     * @param amount New amount of items in this stack
     * @return
     */

    public ItemBuilder setAmount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    /**
     * Add enchantment item builder.
     *
     * @param enchantment        the enchantment
     * @param level              the level
     * @param ignoreRestrictions ignore restrictions
     * @return the item builder
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level,
                                      boolean ignoreRestrictions) {
        itemMeta.addEnchant(enchantment, level, ignoreRestrictions);
        return this;
    }

    /**
     * Add enchantment item builder.
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return the item builder
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        return addEnchantment(enchantment, level, true);
    }

    /**
     * Adds the specified Enchantment to this item stack in an unsafe manner
     * @param enchantment The enchantment to be add
     * @param level The level of the enchantment
     * @return
     */

    @Deprecated
    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Sets the displayname
     * @param name The name to set
     * @return
     */

    public ItemBuilder setDisplayName(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    /**
     * Sets translated display name via the Locale.
     *
     * @param translator the translator
     * @param locale     the locale
     * @param key        the key
     * @param arguments  the arguments
     * @return the item builder
     */
    public ItemBuilder setDisplayName(Translator translator, Locale locale, String key,
                                      Object... arguments) {
        return setDisplayName(translator.getString(locale, key, arguments));
    }

    /**
     * Sets translated display name via the CloudPlayer.
     *
     * @param translator  the translator
     * @param cloudPlayer the cloud player
     * @param key         the key
     * @param arguments   the arguments
     * @return the item builder
     */
    public ItemBuilder setDisplayName(Translator translator, CloudPlayer cloudPlayer, String key,
                                      Object... arguments) {
        return setDisplayName(translator, cloudPlayer.getLocale(), key, arguments);
    }

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     * @param unbreakable true if set unbreakable
     * @return
     */

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Sets the durability of the item.
     *
     * @param durability the new durability value
     * @return
     */
    @Deprecated
    public ItemBuilder setDurability(short durability) {
        this.stack.setDurability(durability);
        return this;
    }

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client
     * @param flag The hideflags which shouldn't be rendered
     * @return
     */

    public ItemBuilder addItemFlag(ItemFlag flag) {
        itemMeta.addItemFlags(flag);
        return this;
    }

    /**
     * Sets the lore for this item. Removes lore when given null
     * @param lore The lore that will be set
     * @return
     */

    public ItemBuilder addLore(String... lore) {
        List<String> currentLore = itemMeta.getLore();
        if (currentLore == null) currentLore = new ArrayList<>();
        currentLore.addAll(Arrays.asList(lore));
        itemMeta.setLore(currentLore);
        return this;
    }

    /**
     * Change a specific line of a lore.
     * @param index The index for the line
     * @param text The new text for the line
     * @return
     */

    public ItemBuilder setLoreLine(int index, String text) {
        List<String> currentLore = itemMeta.getLore();
        if (currentLore == null) {
            currentLore = new ArrayList<>();
            currentLore.add(text);
        }

        if (currentLore.size() < index) {
            currentLore.add(text);
        } else {
            currentLore.set(index, text);
        }
        itemMeta.setLore(currentLore);
        return this;
    }

    /**
     * Sets the lore for this item. Removes lore when given null
     * @param lore The lore that will be set
     * @return
     */

    public ItemBuilder setLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    /**
     * Sets translated lore via the Locale.
     *
     * @param translator the translator
     * @param locale     the locale
     * @param key        the key
     * @param arguments  the arguments
     * @return the item builder
     */
    public ItemBuilder setLore(Translator translator, Locale locale, String key,
                                      Object... arguments) {
        String[] lore = translator.getString(locale, key, arguments).split("\n");
        List<String> loreList = Arrays.asList(lore);
        return setLore(loreList);
    }

    /**
     * Sets translated lore via the CloudPlayer.
     *
     * @param translator  the translator
     * @param cloudPlayer the cloud player
     * @param key         the key
     * @param arguments   the arguments
     * @return the item builder
     */
    public ItemBuilder setLore(Translator translator, CloudPlayer cloudPlayer, String key,
                                      Object... arguments) {
        return setLore(translator, cloudPlayer.getLocale(), key, arguments);
    }

    /**
     * Sets the repair penalty
     * @param repairCosts repair penalty
     * @return
     */

    public ItemBuilder setRepairCosts(int repairCosts) {
        Repairable meta = (Repairable) itemMeta;
        meta.setRepairCost(repairCosts);
        return this;
    }

    /**
     * Set the ItemMeta of this ItemStack
     * @param meta  new ItemMeta, or null to indicate meta data be cleared
     * @return
     */

    private ItemBuilder setItemMeta(ItemMeta meta) {
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Builds the stack
     * @return The stack
     */

    public ItemStack build() {
        stack.setItemMeta(itemMeta);
        return this.stack;
    }

    /**
     * Returns the lore from a item.
     * @return a empty list when the lore is null otherwise the lore from the stack
     */

    public List<String> getLore() {
        if (itemMeta.getLore() == null) {
            return new ArrayList<>();
        }
        return itemMeta.getLore();
    }

    /**
     * Returns from a item the itemmeta
     * @return The itemmeta
     */

    protected ItemMeta getItemMeta() {
        return itemMeta;
    }
}