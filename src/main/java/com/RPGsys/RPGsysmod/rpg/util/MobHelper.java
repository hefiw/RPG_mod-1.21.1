package com.RPGsys.RPGsysmod.rpg.util;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;

public class MobHelper {
    public static boolean isPassive(Mob mob) {
        return !(mob instanceof Enemy);
    }
}
