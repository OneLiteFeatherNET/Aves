package net.theevilreaper.aves.inventory;

import net.theevilreaper.aves.i18n.TextData;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MicrotusExtension.class)
class GlobalTranslatedInventoryBuilderTest {

    @Test
    void testGlobalTranslatedBuilder() {

        var builder = new GlobalTranslatedInventoryBuilder(InventoryType.CHEST_2_ROW);

        builder.setTitleData(new TextData("title"));

        assertNotNull(builder.getTitleData());
        assertNull(builder.getLayout());
        assertNull(builder.getDataLayout());
    }

}
