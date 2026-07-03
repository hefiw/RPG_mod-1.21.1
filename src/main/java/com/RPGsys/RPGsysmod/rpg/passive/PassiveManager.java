package com.RPGsys.RPGsysmod.rpg.passive;

import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import com.RPGsys.RPGsysmod.rpg.util.RPGHelper;
import net.minecraft.world.entity.LivingEntity;

public class PassiveManager {
    public static void generateMissingPassives(LivingEntity entity) {
        RPGData data = RPGHelper.getData(entity);
        int shouldHave = data.getLevel() / 5;
        int current = data.getPassiveSkills().values().stream().mapToInt(Integer::intValue).sum();
        int missing = shouldHave - current;
        if (missing<=0) {return;}

        MobPassiveType[] values = MobPassiveType.values();
        for (int i=0; i<missing+1; i++) {
            MobPassiveType passive = values[entity.getRandom().nextInt(values.length)];
            data.addPassiveSkill(passive);
        }
        PassiveApplier.rebuild(entity);
    }
}
