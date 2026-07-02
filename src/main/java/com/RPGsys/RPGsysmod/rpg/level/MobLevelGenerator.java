package com.RPGsys.RPGsysmod.rpg.level;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;

public class MobLevelGenerator {
    private static final int MIN_EXP = LevelHelper.getExperienceForLevel(1);
    private static final int MAX_EXP = LevelHelper.getExperienceForLevel(200);

    public static int generateExperience(Mob mob) {
        RandomSource random = mob.getRandom();
        double distance = Math.sqrt(mob.getX() * mob.getX() + mob.getZ() * mob.getZ());

        /*
         * Каждые 1000 блоков
         * центр распределения растёт на 1 уровень.
         */

        double mean = 2.0 + distance / 1500.0;
        double maxHealth = mob.getMaxHealth();
        double deltaHealth = 100.0;
        if (maxHealth > deltaHealth) {
            int healthTiers = (int)Math.ceil((maxHealth - deltaHealth) / deltaHealth);
            mean += healthTiers * 20.0;
        }
        double meanExp = LevelHelper.getExperienceForLevel((int)Math.round(mean));

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
