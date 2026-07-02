package com.RPGsys.RPGsysmod.rpg.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;

public class EntityUtil {
    public static boolean isTamed(Entity entity) {

        if (entity instanceof OwnableEntity ownable) {
            return ownable.getOwnerUUID() != null;
        }

        return false;
    }
}
