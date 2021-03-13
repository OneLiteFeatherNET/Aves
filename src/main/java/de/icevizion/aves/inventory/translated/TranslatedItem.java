package de.icevizion.aves.inventory.translated;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.TranslatedObjectCache;
import at.rxcki.strigiformes.message.CompoundMessageCache;
import at.rxcki.strigiformes.text.TextData;
import de.icevizion.aves.item.ItemBuilder;
import de.icevizion.aves.inventory.ItemRegistry;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class TranslatedItem {

    private ItemStack itemStack;
    private MessageProvider messageProvider;
    private TranslatedObjectCache<ItemStack> objectCache;

    private TextData nameTextData;

    private TextData loreTextData;
    private CompoundMessageCache loreCache;

    protected TranslatedItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public TranslatedItem setDisplayName(String key, Object... arguments) {
        this.nameTextData = new TextData(key, arguments);

        return this;
    }

    public TranslatedItem setDisplayName(TextData textData) {
        this.nameTextData = textData;

        return this;
    }

    public TranslatedItem setLore(String key, Object... arguments) {
        return setLore(new TextData(key, arguments));
    }

    public TranslatedItem setLore(TextData textData) {
        if (loreCache != null)
            throw new IllegalStateException("Tried to set simple lore but CompoundMessage is already set");
        loreTextData = textData;

        return this;
    }

    public TranslatedItem setLore(CompoundMessageCache messageCache) {
        if (loreTextData != null)
            throw new IllegalStateException("Tried to set CompoundMessage lore but simple lore is already set");
        loreCache = messageCache;

        return this;
    }

    public ItemStack get(Locale locale) {
        if (locale == null || messageProvider == null)
            return itemStack;

        if (objectCache == null) {
            objectCache = new TranslatedObjectCache<>(locale1 -> {
                var builder = ItemBuilder.ofClone(itemStack)
                        .setDisplayName(messageProvider.getMessage(nameTextData.getKey(),
                                locale1, nameTextData.getArguments()).toJson().toString());

                String lore = null;
                if (loreCache != null) {
                    lore = loreCache.generateMessage(locale1);
                } else if (loreTextData != null){
                    lore = messageProvider.getTextProvider()
                            .format(loreTextData.getKey(), locale1, loreTextData.getArguments());
                }

                if (lore != null)
                    builder.setLore(Arrays.asList(lore.split("\n")));

                var item = builder.build();
                ItemRegistry.register(itemStack, item);

                return item;
            });
        }

        return objectCache.get(locale);
    }

    public MessageProvider getMessageProvider() {
        return messageProvider;
    }

    public TranslatedItem setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;

        return this;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static TranslatedItem of(ItemStack itemStack, MessageProvider messageProvider) {
        TranslatedItem translatedItem = new TranslatedItem(itemStack);
        translatedItem.setMessageProvider(messageProvider);
        //Todo: Register in ItemRegistry

        return translatedItem;
    }

    public static TranslatedItem of(ItemStack itemStack) {
        return TranslatedItem.of(itemStack, null);
    }

    public static TranslatedItem of(ItemBuilder itemBuilder, MessageProvider messageProvider) {
        return TranslatedItem.of(itemBuilder.build(), messageProvider);
    }

    public static TranslatedItem of(ItemBuilder itemBuilder) {
        return TranslatedItem.of(itemBuilder.build(), null);
    }

    public static TranslatedItem empty() {
        return new TranslatedItem(null);
    }

    public TranslatedSlot toSlot(Consumer<InventoryClickEvent> clickListener) {
        return new TranslatedSlot(this, clickListener);
    }
    public TranslatedSlot toSlot() {
        return toSlot(null);
    }
}
