package com.RPGsys.RPGsysmod.rpg.passive;

import net.minecraft.world.entity.LivingEntity;

public interface PassiveEffect {
    void apply(LivingEntity entity, int level);
}
