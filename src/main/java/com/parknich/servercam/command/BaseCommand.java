package com.parknich.servercam.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand implements CommandExecutor {

    private final String permission;
    private final boolean requiresPlayer;

    public BaseCommand(String permission, boolean requiresPlayer) {
        this.permission = permission;
        this.requiresPlayer = requiresPlayer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (requiresPlayer && !(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage("You don't have permission to use this command.");
            return true;
        }

        execute(sender, sender instanceof Player ? (Player) sender : null, label, args);
        return true;
    }

    protected abstract void execute(CommandSender sender, Player player, String label, String[] args);

    public String getPermission() {
        return permission;
    }
}