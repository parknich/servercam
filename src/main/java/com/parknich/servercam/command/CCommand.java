package com.parknich.servercam.command;

import com.parknich.servercam.npc.NpcManager;
import com.parknich.servercam.persistence.Persistence;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CCommand extends BaseCommand {

    private final Persistence persistence;
    private final NpcManager npcManager;

    public CCommand(Persistence persistence, NpcManager npcManager) {
        super(null, true);
        this.persistence = persistence;
        this.npcManager = npcManager;
    }

    @Override
    protected void execute(CommandSender sender, Player player, String label, String[] args) {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        persistence.put(player.getUniqueId(), player.getLocation());
        npcManager.spawnGuardian(player);
        player.setGameMode(GameMode.SPECTATOR);
    }
}