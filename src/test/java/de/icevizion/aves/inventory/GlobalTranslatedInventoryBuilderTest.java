package de.icevizion.aves.inventory;


import de.icevizion.aves.i18n.AvesTranslationRegistry;
import de.icevizion.aves.i18n.TextData;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.Env;
import net.minestom.testing.annotations.EnvironmentTest;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@EnvironmentTest
class GlobalTranslatedInventoryBuilderTest {

    @Test
    void testGlobalTranslatedBuilder() {

        var builder = new GlobalTranslatedInventoryBuilder(InventoryType.CHEST_2_ROW);

        builder.setTitleData(new TextData("title"));

        assertNotNull(builder.getTitleData());
        assertNull(builder.getLayout());
        assertNull(builder.getDataLayout());
    }

    @Test
    void testGlobalTranslatedBuilderWithRegistry(Env env) {
        TranslationRegistry translationRegistry = TranslationRegistry.create(Key.key("test", "test"));
        translationRegistry.register("title", Locale.ENGLISH, new MessageFormat("TEST"));
        GlobalTranslator.translator().addSource(new AvesTranslationRegistry(translationRegistry));

        var builder = new GlobalTranslatedInventoryBuilder(InventoryType.CHEST_2_ROW);

        builder.setTitleData(new TextData("title"));
        builder.setLayout(InventoryLayout.fromType(builder.getType()));
        String serialize = PlainTextComponentSerializer.plainText().serialize(builder.getInventory(Locale.ENGLISH).getTitle());
        assertTrue(serialize.equalsIgnoreCase("TEST"));
        assertNotNull(builder.getTitleData());
        assertNotNull(builder.getLayout());
        assertNull(builder.getDataLayout());
    }


}
