package de.icevizion.aves.resourcepack;

import net.kyori.adventure.resource.ResourcePackInfo;
import net.minestom.server.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResourcePackHandlerTest {

    static final String EMPTY = "";

    final Set<UUID> cache = new HashSet<>();

    @Test
    void testResourcePackConstructorWithForce() {
        final ResourcePackInfo resourcePackInfo = ResourcePackInfo.resourcePackInfo()
                .id(UUID.randomUUID())
                .hash(EMPTY)
                .uri(URI.create(EMPTY))
                .build();
        var handler = new ResourcePackHandler(resourcePackInfo);
        assertNotNull(handler);
    }

    @Test
    void testConstructorWithResourcePack() {
        var resourcePack = Mockito.mock(ResourcePackInfo.class);
        assertNotNull(resourcePack);
        var handler = new ResourcePackHandler(resourcePack);
        assertNotNull(handler);
    }

    @Test
    void testConstructorWithResourcePackAndCondition() {
        var resourcePack = Mockito.mock(ResourcePackInfo.class);
        assertNotNull(resourcePack);
        var handler = new ResourcePackHandler(resourcePack, new DefaultResourcePackCondition(cache));
        assertNotNull(handler);
    }

    @Test
    void testResourcePackHandlerMethods() {
        var resourcePack = Mockito.mock(ResourcePackInfo.class);
        var handler = new ResourcePackHandler(resourcePack);
        var uuid = UUID.randomUUID();
        var player = Mockito.mock(Player.class);

        handler.setDefaultCondition();
        Mockito.when(player.getUuid()).thenReturn(uuid);
        assertTrue(handler.setPack(player));
        handler.invalidateId(player.getUuid());
        assertTrue(cache.isEmpty());
    }

    @Test
    void testResourcePackHandlerConditionSet() {
        var resourcePack = Mockito.mock(ResourcePackInfo.class);
        var handler = new ResourcePackHandler(resourcePack);
        assertDoesNotThrow(() -> handler.setCondition((player, resourcePackStatus) -> { }));
    }

    @Test
    void testResourcePackHandlerWithANullCondition() {
        var resourcePack = Mockito.mock(ResourcePackInfo.class);
        var handler = new ResourcePackHandler(resourcePack);
        assertThrows(
                IllegalStateException.class,
                () -> handler.setCondition(null),
                "Can't register the handler because the 'ResourcePackCondition' is null"
        );
    }
}