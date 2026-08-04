package com.RPGsys.RPGsysmod.rpg.passive;

import net.minecraft.resources.ResourceLocation;

public record PassiveSkillDefinition(
        String id,
        String name,
        String description,
        ResourceLocation icon,
        int maxLevel
) {
}