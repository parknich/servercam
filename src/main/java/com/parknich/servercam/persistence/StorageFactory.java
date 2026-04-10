package com.parknich.servercam.persistence;

import com.parknich.servercam.ServerCam;

public class StorageFactory {

    public static Storage create(ServerCam plugin) {
        String type = plugin.getConfig().getString("persistence-type", "yml").toLowerCase();
        
        switch (type) {
            case "mariadb":
            case "mysql":
                return createDatabaseStorage(plugin);
            case "yml":
            default:
                return new YmlStorage(plugin.getDataFolder());
        }
    }

    private static Storage createDatabaseStorage(ServerCam plugin) {
        String host = plugin.getConfig().getString("persistence.host", "localhost");
        int port = plugin.getConfig().getInt("persistence.port", 3306);
        String database = plugin.getConfig().getString("persistence.database", "servercam");
        String username = plugin.getConfig().getString("persistence.username", "root");
        String password = plugin.getConfig().getString("persistence.password", "");
        String tableName = plugin.getConfig().getString("persistence.table", "servercam");

        try {
            return new MariaDbStorage(host, port, database, username, password, tableName);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
            plugin.getLogger().info("Falling back to YML storage");
            return new YmlStorage(plugin.getDataFolder());
        }
    }
}