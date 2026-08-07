package com.RPGsys.RPGsysmod.rpg.race;

import net.minecraft.world.entity.player.Player;

public class PlayerRaceHelper {

    public static String getRace(Player player) {

        if (player.getTags().contains("vampire")) {
            return "vampire";
        }

        if (player.getTags().contains("pix")) {
            return "pix";
        }

        if (player.getTags().contains("dwarf")) {
            return "dwarf";
        }

        return "human";
    }
}