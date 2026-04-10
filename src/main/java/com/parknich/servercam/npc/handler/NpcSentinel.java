package com.parknich.servercam.npc.handler;

import net.citizensnpcs.api.npc.NPC;
import org.mcmonkey.sentinel.SentinelTrait;

public class NpcSentinel {

    public void apply(NPC npc, double playerHealth) {
        SentinelTrait sentinel = npc.getOrAddTrait(SentinelTrait.class);
        sentinel.health = playerHealth;
        sentinel.invincible = false;
        sentinel.allowKnockback = true;
        sentinel.fightback = false;
        sentinel.runaway = false;
    }
}