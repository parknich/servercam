package com.parknich.servercam.persistence;

import org.bukkit.Location;
import java.util.UUID;

public interface Storage {
    void save(UUID uuid, Location location);
    Location load(UUID uuid);
    void remove(UUID uuid);
    void shutdown();
}