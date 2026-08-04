package com.RPGsys.RPGsysmod.rpg.passive;

import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerPassiveRegistry {
    public static final Map<String, PassiveSkillDefinition> SKILLS = new LinkedHashMap<>();

    public static void register(
            String id,
            String name,
            String desc,
            String texture
    ) {

        SKILLS.put(
                id,
                new PassiveSkillDefinition(
                        id,
                        name,
                        desc,
                        ResourceLocation.fromNamespaceAndPath(
                                "rpgsys",
                                texture
                        ), 100
                )
        );
    }

    public static void init() {
        register(
                "attack_damage",
                "Сила",
                "+5% физического урона",
                "textures/gui/icons/skill-attackDamage.png"
        );
    }
}