package de.icevizion.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText;
import static org.junit.jupiter.api.Assertions.*;

@EnvTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryBuilderTest {

    Component title = Component.text("Title");

    InventoryType type = InventoryType.CHEST_3_ROW;

    GlobalInventoryBuilder builder;

    @BeforeAll
    void onInit(Env env) {
        builder = new GlobalInventoryBuilder(title, type);
        builder.openFunction = event -> {
            throw new RuntimeException("Open function works");
        };
        builder.register();
    }

    @AfterEach
    void onDestroy(Env env) {
        builder.unregister();
    }

    @Test
    void testTitleUpdate(Env env) {
        var currentTitle = plainText().serialize(this.builder.getInventory().getTitle());
        this.builder.setTitleComponent(Component.text("Test 2"));

        var newTitle = plainText().serialize(this.builder.getInventory().getTitle());

        assertNotEquals(currentTitle, newTitle);
    }
}
