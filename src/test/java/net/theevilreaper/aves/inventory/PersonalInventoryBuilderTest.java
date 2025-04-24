package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
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