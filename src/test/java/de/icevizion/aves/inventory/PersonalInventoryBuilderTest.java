package de.icevizion.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
class PersonalInventoryBuilderTest {

    @Test
    void testPersonalBuilder(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);
        var builder = new PersonalInventoryBuilder(Component.text("Test"), InventoryType.CHEST_1_ROW, player);
        builder.setLayout(InventoryLayout.fromType(builder.getType()));
        builder.open();

        builder.register();
        builder.unregister();

        player.remove();
        env.destroyInstance(instance);

        assertNotNull(player);
        assertNotNull(builder);
    }
}