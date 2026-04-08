package com.parknich.servercam;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class ServerCam extends JavaPlugin implements Listener {

    private Persistence persistence;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("c").setExecutor(this);
        getCommand("s").setExecutor(this);

        persistence = new Persistence(this);
        persistence.onEnable();
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                Location saved = persistence.get(player.getUniqueId());
                if (saved != null) {
                    player.teleport(saved);
                }
                player.setGameMode(GameMode.SURVIVAL);
            }
        }
        persistence.onDisable();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SPECTATOR) {
            return;
        }

        Location saved = persistence.get(player.getUniqueId());
        if (saved == null) {
            return;
        }

        double radius = getConfig().getDouble("radius", 150);
        if (player.getLocation().distance(saved) > radius) {
            exitFreecam(player);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("c")) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                return true;
            }
            persistence.put(player.getUniqueId(), player.getLocation());
            player.setGameMode(GameMode.SPECTATOR);
        } else if (label.equalsIgnoreCase("s")) {
            exitFreecam(player);
        }

        return true;
    }

    private void exitFreecam(Player player) {
        Location saved = persistence.remove(player.getUniqueId());
        if (saved != null) {
            player.teleport(saved);
        }
        player.setGameMode(GameMode.SURVIVAL);
    }
}
