package de.icevizion.aves.resourcepack;

/*@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)*/
class ResourcePackHandlerTest {

    /*static final String EMPTY = "";

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
    }*/
}