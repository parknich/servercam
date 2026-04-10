package com.parknich.servercam.npc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NpcData {

    private final Map<UUID, Integer> playerNpcMap = new HashMap<>();
    private final Map<Integer, UUID> npcPlayerMap = new HashMap<>();

    public void add(UUID playerUuid, int npcId) {
        playerNpcMap.put(playerUuid, npcId);
        npcPlayerMap.put(npcId, playerUuid);
    }

    public Integer remove(UUID playerUuid) {
        Integer npcId = playerNpcMap.remove(playerUuid);
        if (npcId != null) {
            npcPlayerMap.remove(npcId);
        }
        return npcId;
    }

    public UUID getPlayerUuid(int npcId) {
        return npcPlayerMap.get(npcId);
    }

    public Integer getNpcId(UUID playerUuid) {
        return playerNpcMap.get(playerUuid);
    }

    public Collection<Integer> getAllIds() {
        return playerNpcMap.values();
    }

    public void clear() {
        playerNpcMap.clear();
        npcPlayerMap.clear();
    }
}