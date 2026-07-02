package com.RPGsys.RPGsysmod.rpg.event;

import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import com.RPGsys.RPGsysmod.rpg.level.MobLevelGenerator;
import com.RPGsys.RPGsysmod.rpg.util.MobHelper;
import com.RPGsys.RPGsysmod.rpg.util.RPGHelper;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class EntitySpawnHandler {
    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinLevelEvent event) {

        if (!(event.getEntity() instanceof Mob mob)) {
            return;
        }

        RPGData data = RPGHelper.getData(mob);

        if (!data.isInitialized()) {

            if (MobHelper.isPassive(mob)) {
                data.setExperience(0);
            } else {
                int exp = MobLevelGenerator.generateExperience(mob);
                data.setExperience(exp);
            }
            data.setInitialized(true);
        }
        EntityNameHandler.updateMobName(mob);
    }
}
