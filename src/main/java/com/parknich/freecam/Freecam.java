package com.parknich.freecam;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Freecam extends JavaPlugin {

    private Map<UUID, Location> savedLocations = new HashMap<>();

    @Override
    public void onEnable() {
        getCommand("c").setExecutor(this);
        getCommand("s").setExecutor(this);
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                Location saved = savedLocations.remove(player.getUniqueId());
                if (saved != null) {
                    player.teleport(saved);
                }
                player.setGameMode(GameMode.SURVIVAL);
            }
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
            savedLocations.put(player.getUniqueId(), player.getLocation());
            player.setGameMode(GameMode.SPECTATOR);
        } else if (label.equalsIgnoreCase("s")) {
            Location saved = savedLocations.remove(player.getUniqueId());
            if (saved == null) {
                return true;
            }
            player.teleport(saved);
            player.setGameMode(GameMode.SURVIVAL);
        }

        return true;
    }
}
