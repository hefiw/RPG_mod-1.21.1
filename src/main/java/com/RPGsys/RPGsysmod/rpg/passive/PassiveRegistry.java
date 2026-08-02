package com.RPGsys.RPGsysmod.rpg.passive;

import com.RPGsys.RPGsysmod.rpg.passive.effects.*;

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

        EFFECTS.put(
                MobPassiveType.SPEED_BOOST,
                new SpeedBoostPassive()
        );

        EFFECTS.put(
                MobPassiveType.TOUGHNESS_BOOST,
                new ToughnessBoostPassive()
        );

        EFFECTS.put(
                MobPassiveType.KNOCKBACK_BOOST,
                new KnockbackBoostPassive()
        );
    }

    public static PassiveEffect get(MobPassiveType type) {return EFFECTS.get(type);}
}
