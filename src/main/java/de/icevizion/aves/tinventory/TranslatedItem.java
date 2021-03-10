package de.icevizion.aves.tinventory;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.TranslatedObjectCache;
import at.rxcki.strigiformes.message.CompoundMessageCache;
import at.rxcki.strigiformes.text.TextData;
import de.icevizion.aves.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class TranslatedItem {

    private final ItemStack itemStack;
    private MessageProvider messageProvider;
    private TranslatedObjectCache<ItemStack> objectCache;

    private TextData nameTextData;

    private TextData loreTextData;
    private CompoundMessageCache loreCache;

    protected TranslatedItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public TranslatedItem displayName(String key, Object... arguments) {
        this.nameTextData = new TextData(key, arguments);

        return this;
    }

    public TranslatedItem displayName(TextData textData) {
        this.nameTextData = textData;

        return this;
    }

    public TranslatedItem lore(String key, Object... arguments) {
        return lore(new TextData(key, arguments));
    }

    public TranslatedItem lore(TextData textData) {
        if (loreCache != null)
            throw new IllegalStateException("Tried to set simple lore but CompoundMessage is already set");
        loreTextData = textData;

        return this;
    }

    public TranslatedItem lore(CompoundMessageCache messageCache) {
        if (loreTextData != null)
            throw new IllegalStateException("Tried to set CompoundMessage lore but simple lore is already set");
        loreCache = messageCache;

        return this;
    }

    public ItemStack get(Locale locale) {
        if (objectCache == null) {
            objectCache = new TranslatedObjectCache<ItemStack>(locale1 -> {
                var builder = ItemBuilder.ofClone(itemStack)
                        .setDisplayName(messageProvider.getMessage(nameTextData.getKey(),
                                locale1, nameTextData.getArguments()).toJson().toString());

                String lore = null;
                if (loreCache != null) {
                    lore = loreCache.generateMessage(locale);
                } else if (loreTextData != null){
                    lore = messageProvider.getTextProvider()
                            .format(loreTextData.getKey(), locale, loreTextData.getArguments());
                }

                if (lore != null)
                    builder.setLore(Arrays.asList(lore.split("\n")));

                return builder.build();
            });
        }

        return objectCache.get(locale);
    }

    public TranslatedItem messageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;

        return this;
    }

    public static TranslatedItem of(ItemStack itemStack, MessageProvider messageProvider) {
        TranslatedItem translatedItem = new TranslatedItem(itemStack);
        translatedItem.messageProvider(messageProvider);
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
}
