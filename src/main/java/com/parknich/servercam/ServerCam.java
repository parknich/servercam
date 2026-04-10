package com.parknich.servercam;

import com.parknich.servercam.command.CommandManager;
import com.parknich.servercam.listener.RadiusListener;
import com.parknich.servercam.listener.SpectatorListener;
import com.parknich.servercam.npc.NpcManager;
import com.parknich.servercam.persistence.Persistence;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerCam extends JavaPlugin {

    private Persistence persistence;
    private CommandManager commandManager;
    private NpcManager npcManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        persistence = new Persistence(this);
        persistence.onEnable();

        npcManager = new NpcManager(this, persistence);
        npcManager.onEnable(this);

        commandManager = new CommandManager(this, npcManager);
        commandManager.registerAll(persistence, npcManager);

        double radius = getConfig().getDouble("radius", 150);
        getServer().getPluginManager().registerEvents(new RadiusListener(persistence, npcManager, radius), this);
        getServer().getPluginManager().registerEvents(new SpectatorListener(), this);
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                npcManager.removeGuardian(event.getPlayer());
            }
        }, this);
    }

    @Override
    public void onDisable() {
        for (org.bukkit.entity.Player player : getServer().getOnlinePlayers()) {
            if (player.getGameMode() == org.bukkit.GameMode.SPECTATOR) {
                npcManager.removeGuardian(player);
                org.bukkit.Location saved = persistence.get(player.getUniqueId());
                if (saved != null) {
                    player.teleport(saved);
                }
                player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                persistence.remove(player.getUniqueId());
            }
        }
        persistence.onDisable();
    }
}