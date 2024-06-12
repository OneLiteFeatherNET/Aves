package de.icevizion.aves.inventory;


import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.annotations.EnvironmentTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnvironmentTest
@ExtendWith(MockitoExtension.class)
class PersonalTranslatedInventoryBuilderTest {

    @Test
    void testConstructor() {
        var player = Mockito.mock(Player.class);
        var builder = new PersonalTranslatedInventoryBuilder(InventoryType.CHEST_2_ROW, player);
        assertNotNull(builder);
    }
}
