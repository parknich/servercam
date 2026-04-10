package com.parknich.servercam.npc.handler;

import com.parknich.servercam.npc.NpcData;
import net.citizensnpcs.api.event.NPCDeathEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class NpcDeathHandler implements Listener {

    private final NpcData data;
    private final PlayerKiller playerKiller;
    private final NpcSpawner spawner;
    private final JavaPlugin plugin;

    public NpcDeathHandler(NpcData data, PlayerKiller playerKiller, NpcSpawner spawner, JavaPlugin plugin) {
        this.data = data;
        this.playerKiller = playerKiller;
        this.spawner = spawner;
        this.plugin = plugin;
    }

    @EventHandler
    public void onNpcDeath(NPCDeathEvent event) {
        UUID playerUuid = data.getPlayerUuid(event.getNPC().getId());
        if (playerUuid == null) return;

        Player player = plugin.getServer().getPlayer(playerUuid);
        if (player == null) return;

        Location deathLocation = event.getNPC().getEntity().getLocation();

        for (ItemStack item : event.getDrops()) {
            if (item != null && !item.getType().isAir()) {
                deathLocation.getWorld().dropItemNaturally(deathLocation, item);
            }
        }
        event.getDrops().clear();

        spawner.remove(playerUuid);
        
        playerKiller.kill(player, deathLocation);
    }
}