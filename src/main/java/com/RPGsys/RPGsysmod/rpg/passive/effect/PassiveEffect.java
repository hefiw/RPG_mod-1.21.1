package com.RPGsys.RPGsysmod.rpg.passive.effect;

import com.RPGsys.RPGsysmod.rpg.passive.MobPassiveType;
import net.minecraft.world.entity.LivingEntity;

public interface PassiveEffect {
    void apply(LivingEntity entity, int level);
}
