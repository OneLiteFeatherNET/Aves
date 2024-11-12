package de.icevizion.aves.inventory;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MicrotusExtension.class)
class PersonalTranslatedInventoryBuilderTest {

    @Test
    void testConstructor(Env env) {
        final Instance instance = env.createFlatInstance();
        final Player player = env.createPlayer(instance, Pos.ZERO);
        var builder = new PersonalTranslatedInventoryBuilder(InventoryType.CHEST_2_ROW, player);
        assertNotNull(builder);
        player.remove();
        env.destroyInstance(instance);
    }
}
