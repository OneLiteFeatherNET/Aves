package de.icevizion.aves.resourcepack;

import net.minestom.server.resourcepack.ResourcePack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResourcePackHandlerTest {

    final Set<UUID> cache = new HashSet<>();

    @Test
    void testConstructorWithResourcePack() {
        var resourcePack = Mockito.mock(ResourcePack.class);
        assertNotNull(resourcePack);
        var handler = new ResourcePackHandler(resourcePack);
        assertNotNull(handler);
    }

    @Test
    void testConstructorWithResourcePackAndCondition() {
        var resourcePack = Mockito.mock(ResourcePack.class);
        assertNotNull(resourcePack);
        var handler = new ResourcePackHandler(resourcePack, new DefaultResourcePackCondition(cache));
        assertNotNull(handler);
    }
}