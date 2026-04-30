package com.parknich.servercam.npc;

import com.parknich.servercam.npc.handler.NpcDeathHandler;
import com.parknich.servercam.npc.handler.NpcEquipment;
import com.parknich.servercam.npc.handler.NpcSentinel;
import com.parknich.servercam.npc.handler.NpcSpawner;
import com.parknich.servercam.npc.handler.PlayerKiller;
import com.parknich.servercam.persistence.Persistence;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class NpcManager {

    private final boolean enabled;
    private final NpcData data;
    private final NpcSpawner spawner;
    private final NpcEquipment equipment;
    private final NpcSentinel sentinel;
    private final PlayerKiller playerKiller;
    private final NpcDeathHandler deathHandler;
    private final Persistence persistence;

    public NpcManager(JavaPlugin plugin, Persistence persistence) {
        this.persistence = persistence;
        
        ConfigurationSection npcConfig = plugin.getConfig().getConfigurationSection("npc");
        boolean configEnabled = npcConfig != null && npcConfig.getBoolean("enabled", false);

        boolean citizensLoaded = plugin.getServer().getPluginManager().isPluginEnabled("Citizens");
        boolean sentinelLoaded = plugin.getServer().getPluginManager().isPluginEnabled("Sentinel");

        this.enabled = configEnabled && citizensLoaded && sentinelLoaded;
        
        this.data = new NpcData();
        this.spawner = new NpcSpawner(data);
        this.equipment = new NpcEquipment();
        this.sentinel = new NpcSentinel();
        this.playerKiller = new PlayerKiller();
        this.deathHandler = new NpcDeathHandler(data, playerKiller, spawner, plugin);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void onEnable(JavaPlugin plugin) {
        if (!enabled) return;
        
        plugin.getServer().getPluginManager().registerEvents(playerKiller, plugin);
        plugin.getServer().getPluginManager().registerEvents(deathHandler, plugin);
    }

    public void onDisable() {
    }

    public void spawnGuardian(Player player) {
        if (!enabled) return;

        spawner.spawn(player);
        
        net.citizensnpcs.api.npc.NPC npc = getNpc(player.getUniqueId());
        if (npc != null) {
            equipment.apply(npc, player);
            sentinel.apply(npc, player.getHealth());
        }
    }

    public void removeGuardian(Player player) {
        if (!enabled) return;
        spawner.remove(player.getUniqueId());
    }

    public void handlePlayerJoin(Player player) {
    }

    public Location getNpcLocation(Player player) {
        if (!enabled) return null;
        net.citizensnpcs.api.npc.NPC npc = getNpc(player.getUniqueId());
        if (npc != null && npc.isSpawned()) {
            return npc.getEntity().getLocation();
        }
        return null;
    }

    private net.citizensnpcs.api.npc.NPC getNpc(java.util.UUID playerUuid) {
        Integer npcId = data.getNpcId(playerUuid);
        if (npcId == null) return null;
        return spawner.getNpcById(npcId);
    }
}