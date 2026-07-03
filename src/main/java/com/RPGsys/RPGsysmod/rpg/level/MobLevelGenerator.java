package com.RPGsys.RPGsysmod.rpg.level;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;

public class MobLevelGenerator {
    private static final int MIN_EXP = LevelHelper.getExperienceForLevel(1);
    private static final int MAX_EXP = LevelHelper.getExperienceForLevel(500);

    public static int generateExperience(Mob mob) {
        RandomSource random = mob.getRandom();
        double distance = Math.sqrt(mob.getX() * mob.getX() + mob.getZ() * mob.getZ());
        double maxHealth = mob.getMaxHealth();
        double attackDamage = 0.0;
        if (mob.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            attackDamage = mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
        }
        double combatRating = maxHealth + attackDamage * 5.0;
        boolean passive = !(mob instanceof Enemy);
        double distanceContribution = passive ? distance / 20000.0 : distance / 1500.0;

        /*
         * Каждые 1000 блоков
         * центр распределения растёт на 1 уровень.
         */

        double mean = 2.0 + distanceContribution + combatRating / 10;
        double meanExp = LevelHelper.getExperienceForLevel((int)Math.round(mean));
        if (passive) { meanExp /= 10.0; }

        /*
         * Ширина распределения.
         */

        double sigma = Math.max(50.0, meanExp * 0.35);
        double gaussian = Math.max(-3.0, Math.min(3.0, random.nextGaussian()));
        int experience = (int)Math.round(meanExp + gaussian * sigma);

        return Math.max(MIN_EXP, Math.min(MAX_EXP, experience)
        );
    }
}
