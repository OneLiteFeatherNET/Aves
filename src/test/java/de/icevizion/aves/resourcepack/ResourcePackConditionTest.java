package de.icevizion.aves.resourcepack;

/*@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)*/
class ResourcePackConditionTest {
/*
    Set<UUID> cache;

    ResourcePackCondition condition;

    Player player;

    @BeforeAll
    void init() {
        this.cache = new HashSet<>();
        this.condition = new DefaultResourcePackCondition(this.cache);
        this.player = Mockito.mock(Player.class);
        this.cache.add(this.player.getUuid());
    }

    @Test
    void testDeclinedResourcePack() {
        this.condition.handleStatus(this.player, ResourcePackStatus.DECLINED);
        assertSame(0, this.cache.size());
        this.cache.add(this.player.getUuid());
    }

    @Test
    void testFailedToDownload() {
        this.condition.handleStatus(this.player, ResourcePackStatus.FAILED_DOWNLOAD);
        assertSame(0, this.cache.size());
        this.cache.add(this.player.getUuid());
    }

    @Test
    void testOtherActions() {
        this.condition.handleStatus(this.player, ResourcePackStatus.ACCEPTED);
        assertSame(1, this.cache.size());
        this.cache.add(this.player.getUuid());
    }*/
}