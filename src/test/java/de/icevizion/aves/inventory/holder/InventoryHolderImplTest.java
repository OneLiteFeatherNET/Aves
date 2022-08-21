package de.icevizion.aves.inventory.holder;

import de.icevizion.aves.inventory.InventoryBuilder;
import net.minestom.server.inventory.Inventory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InventoryHolderImplTest {

    @Test
    void testHolderImpl() {
        var mockedInv = Mockito.mock(Inventory.class);
        var mockedBuilder = Mockito.mock(InventoryBuilder.class);

        Mockito.when(mockedBuilder.getInventory()).thenAnswer(invocation -> mockedInv);

        var holder = new InventoryHolderImpl(mockedBuilder);
        assertEquals(mockedInv, holder.getInventory());
    }
}