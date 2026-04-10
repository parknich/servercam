package com.parknich.servercam.persistence;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class YmlStorage implements Storage {

    private final File dataFile;

    public YmlStorage(File dataFolder) {
        this.dataFile = new File(dataFolder, "data.yml");
    }

    @Override
    public void save(UUID uuid, Location location) {
        YamlConfiguration config = loadConfig();
        String key = uuid.toString();
        config.set(key + ".world", location.getWorld().getName());
        config.set(key + ".x", location.getX());
        config.set(key + ".y", location.getY());
        config.set(key + ".z", location.getZ());
        config.set(key + ".yaw", location.getYaw());
        config.set(key + ".pitch", location.getPitch());
        saveConfig(config);
    }

    @Override
    public Location load(UUID uuid) {
        if (!dataFile.exists()) {
            return null;
        }
        YamlConfiguration config = loadConfig();
        String key = uuid.toString();
        if (!config.contains(key)) {
            return null;
        }
        String world = config.getString(key + ".world");
        double x = config.getDouble(key + ".x");
        double y = config.getDouble(key + ".y");
        double z = config.getDouble(key + ".z");
        float yaw = (float) config.getDouble(key + ".yaw");
        float pitch = (float) config.getDouble(key + ".pitch");
        World bukkitWorld = Bukkit.getWorld(world);
        return bukkitWorld != null ? new Location(bukkitWorld, x, y, z, yaw, pitch) : null;
    }

    @Override
    public void remove(UUID uuid) {
        YamlConfiguration config = loadConfig();
        config.set(uuid.toString(), null);
        saveConfig(config);
    }

    @Override
    public void shutdown() {
    }

    private YamlConfiguration loadConfig() {
        return YamlConfiguration.loadConfiguration(dataFile);
    }

    private void saveConfig(YamlConfiguration config) {
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}