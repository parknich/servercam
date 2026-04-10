package com.parknich.servercam.npc.handler;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.entity.Player;

public class NpcEquipment {

    public void apply(NPC npc, Player player) {
        Equipment equipment = npc.getOrAddTrait(Equipment.class);
        equipment.set(Equipment.EquipmentSlot.HELMET, player.getInventory().getHelmet());
        equipment.set(Equipment.EquipmentSlot.CHESTPLATE, player.getInventory().getChestplate());
        equipment.set(Equipment.EquipmentSlot.LEGGINGS, player.getInventory().getLeggings());
        equipment.set(Equipment.EquipmentSlot.BOOTS, player.getInventory().getBoots());
        equipment.set(Equipment.EquipmentSlot.HAND, player.getInventory().getItemInMainHand());
        equipment.set(Equipment.EquipmentSlot.OFF_HAND, player.getInventory().getItemInOffHand());
    }
}