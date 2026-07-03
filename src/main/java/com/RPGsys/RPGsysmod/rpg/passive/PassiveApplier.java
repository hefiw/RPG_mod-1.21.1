package com.RPGsys.RPGsysmod.rpg.passive;

import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import com.RPGsys.RPGsysmod.rpg.passive.effect.PassiveEffect;
import com.RPGsys.RPGsysmod.rpg.util.RPGHelper;
import net.minecraft.world.entity.LivingEntity;

public class PassiveApplier {
    public static void rebuild(LivingEntity entity) {

        RPGData data = RPGHelper.getData(entity);

        for (var entry : data.getPassiveSkills().entrySet()) {
            PassiveEffect effect = PassiveRegistry.get(entry.getKey());
            if (effect == null) {continue;}
            effect.apply(entity, entry.getValue());
        }
    }
}
