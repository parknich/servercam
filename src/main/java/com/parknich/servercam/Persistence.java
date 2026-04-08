package com.parknich.servercam;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Persistence implements Listener {

    private final ServerCam plugin;
    private final Map<UUID, Location> savedLocations = new HashMap<>();
    private final File dataFile;

    public Persistence(ServerCam plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
    }

    public void onEnable() {
        if (!plugin.getConfig().getBoolean("persistence", false)) {
            return;
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        load();
    }

    public void onDisable() {
        if (!plugin.getConfig().getBoolean("persistence", false)) {
            return;
        }
        save();
    }

    private void load() {
        if (!dataFile.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        for (String key : config.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            String world = config.getString(key + ".world");
            double x = config.getDouble(key + ".x");
            double y = config.getDouble(key + ".y");
            double z = config.getDouble(key + ".z");
            float yaw = (float) config.getDouble(key + ".yaw");
            float pitch = (float) config.getDouble(key + ".pitch");

            World bukkitWorld = Bukkit.getWorld(world);
            if (bukkitWorld != null) {
                savedLocations.put(uuid, new Location(bukkitWorld, x, y, z, yaw, pitch));
            }
        }
    }

    private void save() {
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, Location> entry : savedLocations.entrySet()) {
            String key = entry.getKey().toString();
            Location loc = entry.getValue();
            config.set(key + ".world", loc.getWorld().getName());
            config.set(key + ".x", loc.getX());
            config.set(key + ".y", loc.getY());
            config.set(key + ".z", loc.getZ());
            config.set(key + ".yaw", loc.getYaw());
            config.set(key + ".pitch", loc.getPitch());
        }

        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(UUID uuid, Location location) {
        savedLocations.put(uuid, location);
    }

    public Location get(UUID uuid) {
        return savedLocations.get(uuid);
    }

    public Location remove(UUID uuid) {
        return savedLocations.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        return savedLocations.containsKey(uuid);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (savedLocations.containsKey(player.getUniqueId())) {
            Location saved = savedLocations.remove(player.getUniqueId());
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            player.teleport(saved);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    }
}
