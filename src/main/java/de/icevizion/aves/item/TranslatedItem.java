package de.icevizion.aves.item;

import de.icevizion.aves.i18n.TextData;
import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.inventory.slot.TranslatedSlot;
import de.icevizion.aves.inventory.util.InventoryConstants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The TranslatedItem is a class which holds an {@link ItemStack} with a name and lore which can be translated.
 * @author Patrick Zdarsky / Rxcki
 * @version 2.0.0
 * @since 1.0.13
 * @see IItem
 */
@SuppressWarnings("java:S3252")
public non-sealed class TranslatedItem implements IItem {

    private final Map<Locale, ItemStack> objectCache;
    private ItemStack itemStack;
    private TextData nameTextData;
    private TextData loreTextData;

    /**
     * Creates a new instance of the {@link TranslatedItem}.
     * The constructor is protected because there are some static factory methods to create a new instance.
     *
     * @param itemStack the {@link ItemStack} to set
     */
    protected TranslatedItem(@NotNull ItemStack itemStack) {
        Check.argCondition(itemStack == ItemStack.AIR || itemStack.material() == Material.AIR, "ItemStack can't be from type air");
        this.itemStack = itemStack;
        this.objectCache = new HashMap<>();
    }

    /**
     * Returns a new TranslatedItem with the given {@link ItemStack}.
     * @param itemStack the stack to set
     * @return the created {@link TranslatedItem}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull TranslatedItem of(@NotNull ItemStack itemStack) {
        return new TranslatedItem(itemStack);
    }

    /**
     * Returns a new TranslatedItem with the given {@link ItemStack.Builder}.
     * @param itemBuilder the builder to set
     * @return the created {@link TranslatedItem}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull TranslatedItem of(@NotNull ItemStack.Builder itemBuilder) {
        return TranslatedItem.of(itemBuilder.build());
    }

    /**
     * Creates a new instance of the {@link TranslatedItem} with the given {@link Material}.
     * @param material the material to set
     * @return the created {@link TranslatedItem}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull TranslatedItem of(@NotNull Material material) {
        return of(ItemStack.of(material));
    }

    /**
     * Set the displayName for the {@link ItemStack}.
     *
     * @param key       The key for the name which is located in a language file
     * @param arguments Arguments that may be needed for the name
     * @return TranslatedItem
     */
    public @NotNull TranslatedItem setDisplayName(@NotNull String key, String... arguments) {
        this.nameTextData = new TextData(key, arguments);
        return this;
    }

    /**
     * Set the displayname for the {@link ItemStack}.
     *
     * @param textData The displayname as {@link TextData}
     * @return TranslatedItem
     */
    public @NotNull TranslatedItem setDisplayName(@NotNull TextData textData) {
        this.nameTextData = textData;
        return this;
    }

    /**
     * Sets the lore for the {@link ItemStack}.
     *
     * @param key       The key for the name which is located in a {@link Locale} file
     * @param arguments Arguments for the lore. The array is optional
     * @return TranslatedItem
     */
    public @NotNull TranslatedItem setLore(@NotNull String key, Component... arguments) {
        return setLore(new TextData(key, arguments));
    }

    /**
     * Sets the lore for the {@link ItemStack}.
     *
     * @param key       The key for the name which is located in a {@link Locale} file
     * @param arguments Arguments for the lore. The array is optional
     * @return TranslatedItem
     */
    public @NotNull TranslatedItem setLore(@NotNull String key, String... arguments) {
        return setLore(new TextData(key, arguments));
    }

    /**
     * Set the lore for the {@link ItemStack}.
     *
     * @param textData the lore as {@link TextData}
     * @return TranslatedItem
     */
    public @NotNull TranslatedItem setLore(TextData textData) {
        loreTextData = textData;
        return this;
    }

    /**
     * Returns a {@link TranslatedSlot} which includes the underlying {@link ItemStack} without a given clickListener.
     *
     * @param clickListener The {@link InventoryClick} to set
     * @return the created {@link TranslatedSlot}
     */
    public @NotNull TranslatedSlot toSlot(@Nullable InventoryClick clickListener) {
        return new TranslatedSlot(this, clickListener);
    }

    /**
     * Returns a {@link TranslatedSlot} which includes the underlying {@link ItemStack} without an event handler.
     *
     * @return the created {@link TranslatedSlot}
     */
    public @NotNull TranslatedSlot toSlot() {
        return toSlot(null);
    }

    /**
     * Returns a {@link TranslatedSlot} which is not clickable .
     *
     * @return the created {@link TranslatedSlot}
     */
    public @NotNull TranslatedSlot toNonClickSlot() {
        return new TranslatedSlot(this, InventoryConstants.CANCEL_CLICK);
    }

    /**
     * Overrides / Sets the {@link ItemStack} for the item.
     *
     * @param itemStack The stack to set
     */
    public void setItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Throws a {@link IllegalCallerException} because the method call is not allowed in the context.
     *
     * @return a {@link IllegalCallerException}
     */
    @Override
    public ItemStack get() {
        throw new UnsupportedOperationException("Can not get item without a locale in a translated context");
    }

    /**
     * Builds the {@link ItemStack} for the given {@link Locale}.
     * If the {@link ItemStack} already exists it will be returned immediately
     *
     * @param locale The locale to get the right stack
     * @return the fetched {@link ItemStack}
     */
    @Override
    public ItemStack get(@NotNull Locale locale) {
        return this.objectCache.computeIfAbsent(locale, locale1 -> {
            ItemStack.Builder builder = ItemStack.builder(itemStack.material());
            if (this.nameTextData != null) {
                final Component textData = Component.translatable(nameTextData.key(), nameTextData.args());
                builder.customName(GlobalTranslator.render(textData, locale1));
            }

            if (loreTextData != null) {
                final Component textData = Component.translatable(loreTextData.key(), loreTextData.args());
                builder.lore(GlobalTranslator.render(textData, locale1));
            }

            return builder.build();
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslatedItem that = (TranslatedItem) o;
        return this.nameTextData.equals(that.nameTextData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTextData);
    }
}
