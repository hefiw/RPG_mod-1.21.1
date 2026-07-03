package com.RPGsys.RPGsysmod.rpg.passive;

import com.RPGsys.RPGsysmod.rpg.passive.effect.ArmorBoostPassive;
import com.RPGsys.RPGsysmod.rpg.passive.effect.DamageBoostPassive;
import com.RPGsys.RPGsysmod.rpg.passive.effect.HealthBoostPassive;
import com.RPGsys.RPGsysmod.rpg.passive.effect.PassiveEffect;

import java.util.EnumMap;

public class PassiveRegistry {
    private static final EnumMap<MobPassiveType, PassiveEffect> EFFECTS = new EnumMap<>(MobPassiveType.class);

    static {
        EFFECTS.put(
                MobPassiveType.HEALTH_BOOST,
                new HealthBoostPassive()
        );

        EFFECTS.put(
                MobPassiveType.DAMAGE_BOOST,
                new DamageBoostPassive()
        );

        EFFECTS.put(
                MobPassiveType.ARMOR_BOOST,
                new ArmorBoostPassive()
        );
    }

    public static PassiveEffect get(MobPassiveType type) {return EFFECTS.get(type);}
}
