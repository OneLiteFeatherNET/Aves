package de.icevizion.aves.resourcepack;

import net.minestom.server.entity.Player;
import net.minestom.server.resourcepack.ResourcePackStatus;
import org.junit.jupiter.api.BeforeAll;
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
class ResourcePackConditionTest {

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
        this.condition.handleStatus(this.player, ResourcePackStatus.SUCCESS);
        assertSame(1, this.cache.size());
        this.cache.add(this.player.getUuid());
    }
}