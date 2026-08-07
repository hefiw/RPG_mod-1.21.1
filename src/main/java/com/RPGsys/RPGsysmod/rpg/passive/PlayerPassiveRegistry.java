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

        register("attack_damage", "Сила", "+5% физического урона за уровень", "skill_attack_damage");
        register("health", "Живучесть", "+1 сердце к максимуму здоровья за уровень", "skill_health");
        register("speed", "Ловкость", "+2% скорости передвижения за уровень", "skill_speed");
        register("regeneration", "Регенерация", "+0.05 HP/сек за уровень", "skill_regeneration");
        register("max_mana_count", "Магический резерв", "+25 максимальной маны за уровень", "skill_max_mana");
        register("mana_regeneration", "Концентрация", "+5% восстановления маны за уровень", "skill_mana_regeneration");
        register("spell_damage", "Могущество магии", "+3% силы заклинаний за уровень", "skill_spell_damage");
        register("resistance", "Стойкость", "-2% входящего физического урона за уровень", "skill_resistance");
        register("magic_resistance", "Магическая защита", "-2% входящего магического урона за уровень", "skill_magic_resistance");
    }
}