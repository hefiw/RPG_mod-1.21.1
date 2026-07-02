package com.RPGsys.RPGsysmod.rpg.util;

import com.RPGsys.RPGsysmod.rpg.attachment.ModAttachments;
import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import net.minecraft.world.entity.LivingEntity;

public class RPGHelper {
    public static RPGData getData(LivingEntity entity) {
        return entity.getData(ModAttachments.RPG_DATA);
    }
}
