package com.parknich.servercam.npc.handler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerKiller implements Listener {

    private final Map<UUID, Location> deathLocations = new HashMap<>();

    public void kill(Player player, Location deathLocation) {
        deathLocations.put(player.getUniqueId(), deathLocation);
        
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                deathLocation.getWorld().dropItemNaturally(deathLocation, item);
            }
        }
        
        player.getInventory().clear();
        player.setHealth(0);
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.sendMessage("Your guardian NPC died! You have been slain.");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        //ugly hack to remove duplicate death message
        UUID playerUuid = event.getEntity().getUniqueId();
        if (deathLocations.containsKey(playerUuid)) {
            event.setDeathMessage(null);
            deathLocations.remove(playerUuid);
        }
    }
}