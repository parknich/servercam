package com.parknich.servercam.command;

import com.parknich.servercam.npc.NpcManager;
import com.parknich.servercam.persistence.Persistence;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SCommand extends BaseCommand {

    private final Persistence persistence;
    private final NpcManager npcManager;

    public SCommand(Persistence persistence, NpcManager npcManager) {
        super(null, true);
        this.persistence = persistence;
        this.npcManager = npcManager;
    }

    @Override
    protected void execute(CommandSender sender, Player player, String label, String[] args) {
        Location npcLocation = npcManager.getNpcLocation(player);
        Location saved = persistence.get(player.getUniqueId());
        
        npcManager.removeGuardian(player);
        persistence.remove(player.getUniqueId());
        
        Location teleportTarget = npcLocation != null ? npcLocation : saved;
        
        if (teleportTarget != null) {
            player.teleport(teleportTarget);
        }
        player.setGameMode(GameMode.SURVIVAL);
    }
}