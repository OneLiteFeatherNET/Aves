package de.icevizion.aves.npc;

import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class NPCPool {

    private static final Team NPC_TEAM;

    private final Map<UUID, NPC> npcMap;

    static {
        NPC_TEAM = MinecraftServer.getTeamManager().createTeam(
                "NPC-TEAM");
        NPC_TEAM.setNameTagVisibility(TeamsPacket.NameTagVisibility.NEVER);
    }

    public NPCPool() {
        this.npcMap = new HashMap<>();
    }

    public void add(@NotNull NPC npc) {
        this.npcMap.put(npc.getUuid(), npc);
    }

    public void remove(@NotNull UUID uuid) {
        this.npcMap.remove(uuid);
    }

    @Nullable
    NPC getNPC(@NotNull UUID uuid) {
        return this.npcMap.get(uuid);
    }
}
