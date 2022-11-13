package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
@ExtendWith(MockitoExtension.class)
class PersonalTranslatedInventoryBuilderTest {

    @Test
    void testConstructor(Env env) {
        var provider = Mockito.mock(MessageProvider.class);
        var player = Mockito.mock(Player.class);
        var builder = new PersonalTranslatedInventoryBuilder(InventoryType.CHEST_2_ROW, provider, player);
        assertNotNull(builder);
    }
}