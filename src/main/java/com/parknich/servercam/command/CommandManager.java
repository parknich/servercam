package com.parknich.servercam.command;

import com.parknich.servercam.npc.NpcManager;
import com.parknich.servercam.persistence.Persistence;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final JavaPlugin plugin;
    private final Map<String, BaseCommand> commands = new HashMap<>();

    public CommandManager(JavaPlugin plugin, NpcManager npcManager) {
        this.plugin = plugin;
    }

    public void registerAll(Persistence persistence, NpcManager npcManager) {
        register("c", new CCommand(persistence, npcManager));
        register("s", new SCommand(persistence, npcManager));
    }

    private void register(String name, BaseCommand executor) {
        PluginCommand command = plugin.getCommand(name);
        if (command != null) {
            command.setExecutor(executor);
            commands.put(name, executor);
        }
    }

    public Map<String, BaseCommand> getCommands() {
        return commands;
    }
}