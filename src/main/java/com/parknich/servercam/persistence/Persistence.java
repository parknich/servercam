package com.parknich.servercam.persistence;

import com.parknich.servercam.ServerCam;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class Persistence implements Listener {

    private final ServerCam plugin;
    private final Storage storage;

    public Persistence(ServerCam plugin) {
        this.plugin = plugin;
        this.storage = StorageFactory.create(plugin);
    }

    public void onEnable() {
        if (plugin.getConfig().getBoolean("persistence.enabled", true)) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    public void onDisable() {
        storage.shutdown();
    }

    public void put(UUID uuid, Location location) {
        storage.save(uuid, location);
    }

    public Location get(UUID uuid) {
        return storage.load(uuid);
    }

    public Location remove(UUID uuid) {
        Location location = storage.load(uuid);
        storage.remove(uuid);
        return location;
    }

    public boolean contains(UUID uuid) {
        return storage.load(uuid) != null;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location saved = remove(player.getUniqueId());
        if (saved != null) {
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            player.teleport(saved);
        }
    }
}