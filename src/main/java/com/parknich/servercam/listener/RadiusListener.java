package com.parknich.servercam.listener;

import com.parknich.servercam.npc.NpcManager;
import com.parknich.servercam.persistence.Persistence;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class RadiusListener implements Listener {

    private final Persistence persistence;
    private final NpcManager npcManager;
    private final double radius;

    public RadiusListener(Persistence persistence, NpcManager npcManager, double radius) {
        this.persistence = persistence;
        this.npcManager = npcManager;
        this.radius = radius;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != org.bukkit.GameMode.SPECTATOR) {
            return;
        }

        org.bukkit.Location saved = persistence.get(player.getUniqueId());
        if (saved == null) {
            return;
        }

        if (player.getLocation().distance(saved) > radius) {
            exitFreecam(player);
        }
    }

    private void exitFreecam(Player player) {
        npcManager.removeGuardian(player);
        org.bukkit.Location saved = persistence.remove(player.getUniqueId());
        if (saved != null) {
            player.teleport(saved);
        }
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
    }
}