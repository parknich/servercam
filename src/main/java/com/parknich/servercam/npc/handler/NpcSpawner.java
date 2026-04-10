package com.parknich.servercam.npc.handler;

import com.parknich.servercam.npc.NpcData;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NpcSpawner {

    private final NpcData data;
    private final NPCRegistry registry;

    public NpcSpawner(NpcData data) {
        this.data = data;
        this.registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
    }

    public void spawn(Player player) {
        NPC npc = registry.createNPC(org.bukkit.entity.EntityType.PLAYER, player.getName() + "'s Guardian");
        
        npc.setName(player.getName());
        npc.setProtected(false);
        
        npc.spawn(player.getLocation());
        
        data.add(player.getUniqueId(), npc.getId());
    }

    public void remove(UUID playerUuid) {
        Integer npcId = data.remove(playerUuid);
        if (npcId == null) return;
        
        NPC npc = registry.getById(npcId);
        if (npc != null) {
            npc.despawn();
            npc.destroy();
        }
    }

    public void removeAll() {
        for (int npcId : data.getAllIds()) {
            NPC npc = registry.getById(npcId);
            if (npc != null) {
                npc.despawn();
                npc.destroy();
            }
        }
        data.clear();
    }

    public NPC getNpcById(int npcId) {
        return registry.getById(npcId);
    }
}