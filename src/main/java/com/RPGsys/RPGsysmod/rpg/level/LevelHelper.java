package com.RPGsys.RPGsysmod.rpg.level;

public class LevelHelper {
    public static int getLevelFromExperience(int experience) {
        return (int) Math.floor(-0.5D + Math.sqrt(0.2D * experience + 2.25D));
    }

    public static int getExperienceForLevel(int level) {
        return 5 * level * (level + 1);
    }
}
