package com.RPGsys.RPGsysmod.rpg.passive;

import com.RPGsys.RPGsysmod.ExampleMod;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerPassiveRegistry {
    public static final Map<String, PassiveSkillDefinition> SKILLS = new LinkedHashMap<>();

    public static void register(String id, String name, String desc, String iconName) {

        SKILLS.put(
                id,
                new PassiveSkillDefinition(
                        id,
                        name,
                        desc,
                        buildSkillIcon(iconName),
                        100
                )
        );
    }

    public static ResourceLocation buildSkillIcon(String iconName) {
        return ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID,
                "textures/gui/icons/" + iconName + ".png"
        );
    }
    public static void init() {
        if (!SKILLS.isEmpty()) {
            return;
        }

        register("attack_damage", "Сила", "+5% физического урона", "skill_attack_damage");
        register("health", "Живучесть", "+1 сердце к максимуму здоровья", "skill_health");
        register("armor", "Броня", "+1 к броне", "skill_armor");
        register("speed", "Скорость", "+2% скорости передвижения", "skill_speed");
        register("toughness", "Стойкость", "+1 к твёрдости брони", "skill_toughness");
        register("knockback", "Натиск", "+5% отбрасывания", "skill_knockback");
    }
}