package com.RPGsys.RPGsysmod.rpg.event;

import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import com.RPGsys.RPGsysmod.rpg.util.RPGHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;

public class EntityNameHandler {
    public static void updateMobName(Mob mob) {

        RPGData data = RPGHelper.getData(mob);

        String originalName = mob.getType().getDescription().getString();

        mob.setCustomName(
                Component.literal(
                        "[Lv." + data.getLevel() + "] " + originalName
                )
        );

        mob.setCustomNameVisible(true);
    }
}
