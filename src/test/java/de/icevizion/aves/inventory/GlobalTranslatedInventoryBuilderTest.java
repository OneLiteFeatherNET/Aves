package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.text.TextData;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@EnvTest
class GlobalTranslatedInventoryBuilderTest {

    @Test
    void testGlobalTranslatedBuilder(Env env) {
        var messageProvider = Mockito.mock(MessageProvider.class);

        var builder = new GlobalTranslatedInventoryBuilder(InventoryType.CHEST_2_ROW, messageProvider);

        builder.setTitleData(new TextData("title"));

        assertNotNull(builder.getTitleData());
        assertNull(builder.getLayout());
        assertNull(builder.getDataLayout());
    }
}