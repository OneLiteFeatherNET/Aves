package de.icevizion.aves.resourcepack;

import net.kyori.adventure.resource.ResourcePackStatus;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MicrotusExtension.class)
class ResourcePackConditionIntegrationTest {

    private static Set<UUID> cache;
    private static ResourcePackCondition condition;

    @BeforeAll
    static void init() {
        cache = new HashSet<>();
        condition = new DefaultResourcePackCondition(cache);
    }

    @AfterEach
    void tearDownEach() {
        cache.clear();
    }

    @AfterAll
    static void tearDown() {
        cache = null;
        condition = null;
    }

    @Test
    void testDeclinedResourcePack(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        cache.add(player.getUuid());

        condition.handleStatus(player, ResourcePackStatus.DECLINED);
        assertSame(0, cache.size());
        env.destroyInstance(instance, true);
    }

    @Test
    void testFailedToDownload(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        cache.add(player.getUuid());
        condition.handleStatus(player, ResourcePackStatus.FAILED_DOWNLOAD);
        assertSame(0, cache.size());
        env.destroyInstance(instance, true);
    }

    @Test
    void testOtherActions(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        cache.add(player.getUuid());

        condition.handleStatus(player, ResourcePackStatus.ACCEPTED);
        assertSame(1, cache.size());
        env.destroyInstance(instance, true);
    }
}
