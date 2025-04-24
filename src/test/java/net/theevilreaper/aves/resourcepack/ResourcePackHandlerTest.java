package net.theevilreaper.aves.resourcepack;

import net.kyori.adventure.resource.ResourcePackInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourcePackHandlerTest {

    static final String EMPTY = "";
    private static ResourcePackInfo testResourcePackInfo;
    private static Set<UUID> cache;

    @BeforeAll
    static void init() {
        testResourcePackInfo = ResourcePackInfo.resourcePackInfo()
                .id(UUID.randomUUID())
                .hash(EMPTY)
                .uri(URI.create(EMPTY))
                .build();
        cache = new HashSet<>();
    }

    @Test
    void testConstructorWithResourcePack() {
        var handler = new ResourcePackHandler(testResourcePackInfo);
        assertNotNull(handler);
    }

    @Test
    void testConstructorWithResourcePackAndCondition() {
        var handler = new ResourcePackHandler(testResourcePackInfo, new DefaultResourcePackCondition(cache));
        assertNotNull(handler);
    }

    @Test
    void testResourcePackHandlerConditionSet() {
        var handler = new ResourcePackHandler(testResourcePackInfo);
        assertDoesNotThrow(() -> handler.setCondition((player, resourcePackStatus) -> { }));
    }

    @Test
    void testResourcePackHandlerWithANullCondition() {
        var handler = new ResourcePackHandler(testResourcePackInfo);
        assertThrows(
                IllegalStateException.class,
                () -> handler.setCondition(null),
                "Can't register the handler because the 'ResourcePackCondition' is null"
        );
    }
}