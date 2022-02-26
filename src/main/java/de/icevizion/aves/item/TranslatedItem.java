package de.icevizion.aves.item;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.TranslatedObjectCache;
import at.rxcki.strigiformes.message.CompoundMessageCache;
import at.rxcki.strigiformes.text.TextData;
import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.slot.TranslatedSlot;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.ItemStackBuilder;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public non-sealed class TranslatedItem implements IItem {

    private ItemStack itemStack;
    private MessageProvider messageProvider;
    private TranslatedObjectCache<ItemStack> objectCache;
    private TextData nameTextData;
    private TextData loreTextData;
    private CompoundMessageCache loreCache;

    protected TranslatedItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static TranslatedItem of(@NotNull ItemStack itemStack, MessageProvider messageProvider) {
        TranslatedItem translatedItem = new TranslatedItem(itemStack);
        translatedItem.setMessageProvider(messageProvider);
        return translatedItem;
    }

    public static TranslatedItem of(@NotNull ItemStack itemStack) {
        return TranslatedItem.of(itemStack, null);
    }

    public static TranslatedItem of(@NotNull ItemStackBuilder itemBuilder, MessageProvider messageProvider) {
        return TranslatedItem.of(itemBuilder.build(), messageProvider);
    }

    public static TranslatedItem of(@NotNull Material material) {
        return of(ItemStack.of(material), null);
    }

    public static TranslatedItem of(@NotNull ItemStackBuilder itemBuilder) {
        return TranslatedItem.of(itemBuilder.build(), null);
    }

    public static TranslatedItem empty() {
        return new TranslatedItem(null);
    }

    /**
     * Set the displayName for the {@link ItemStack}.
     * @param key The key for the name which is located in a language file
     * @param arguments Arguments that may be needed for the name
     * @return
     */
    public TranslatedItem setDisplayName(@NotNull String key, Object... arguments) {
        this.nameTextData = new TextData(key, arguments);
        return this;
    }

    /**
     * Set the displayname for the {@link ItemStack}.
     * @param textData The displayname as {@link TextData}
     * @return
     */
    public TranslatedItem setDisplayName(@NotNull TextData textData) {
        this.nameTextData = textData;
        return this;
    }

    /**
     * Sets the lore for the {@link ItemStack}.
     * @param key The key for the name which is located in a {@link Locale} file
     * @param arguments Arguments for the lore. The array is optional
     * @return
     */
    public TranslatedItem setLore(@NotNull String key, Object... arguments) {
        return setLore(new TextData(key, arguments));
    }

    /**
     * Set the lore for the {@link ItemStack}.
     * @param textData the lore as {@link TextData}
     * @return
     */
    public TranslatedItem setLore(TextData textData) {
        if (loreCache != null)
            throw new IllegalStateException("Tried to set simple lore but CompoundMessage is already set");
        loreTextData = textData;
        return this;
    }

    /**
     * Set the lore for the {@link ItemStack}.
     * @param messageCache The lore as {@link CompoundMessageCache}
     * @return
     */
    public TranslatedItem setLore(CompoundMessageCache messageCache) {
        if (loreTextData != null)
            throw new IllegalStateException("Tried to set CompoundMessage lore but simple lore is already set");
        loreCache = messageCache;
        return this;
    }

    /**
     * Overrides / Sets the {@link MessageProvider} for the item.
     * @param messageProvider The provider to set
     * @return
     */
    public TranslatedItem setMessageProvider(@NotNull MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
        return this;
    }

    /**
     * Returns a {@link TranslatedSlot} which includes the underlying {@link ItemStack} without a given clickListener.
     * @param clickListener The {@link InventoryPreClickEvent} to set
     * @return the created {@link TranslatedSlot}
     */
    public TranslatedSlot toSlot(Consumer<InventoryPreClickEvent> clickListener) {
        return new TranslatedSlot(this, clickListener);
    }

    /**
     * Returns a {@link TranslatedSlot} which includes the underlying {@link ItemStack} without a event handler.
     * @return the created {@link TranslatedSlot}
     */
    public TranslatedSlot toSlot() {
        return toSlot(null);
    }

    /**
     * Returns a {@link TranslatedSlot} which is not clickable .
     * @return the created {@link TranslatedSlot}
     */
    public TranslatedSlot toNonClickSlot() {
        return new TranslatedSlot(this, InventoryLayout.CANCEL_CONSUMER);
    }

    /**
     * Overrides / Sets the {@link ItemStack} for the item.
     * @param itemStack The stack to set
     */
    public void setItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Builds the {@link ItemStack} for the given {@link Locale}.
     * If the {@link ItemStack} already exists it will be returned immediately
     * @param locale The locale to get the right stack
     * @return the fetched {@link ItemStack}
     */
    @Override
    public ItemStack get(@NotNull Locale locale) {
        if (messageProvider == null)
            return itemStack;

        if (objectCache == null) {
            objectCache = new TranslatedObjectCache<>(locale1 -> {
                ItemStackBuilder builder = ItemStack.builder(itemStack.getMaterial());

                if (nameTextData != null) {
                    builder.displayName(LegacyComponentSerializer.legacySection().deserialize
                            (messageProvider.getTextProvider().format(nameTextData.getKey(), locale1, nameTextData.getArguments())));
                }


                TextComponent lore = null;

                if (loreCache != null) {
                    lore = LegacyComponentSerializer.legacySection().deserialize(loreCache.generateMessage(locale1));
                } else if (loreTextData != null){
                    lore = LegacyComponentSerializer.legacySection().deserialize(
                            messageProvider.getTextProvider().format(loreTextData.getKey(), locale1, loreTextData.getArguments()));
                }

                if (lore != null) {
                    builder.lore(lore);
                }

                return builder.build();
            });
        }

        return objectCache.get(locale);
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

    /**
     * Returns the given instance from the {@link MessageProvider}.
     * @return The underlying value
     */
    public MessageProvider getMessageProvider() {
        return messageProvider;
    }
}
