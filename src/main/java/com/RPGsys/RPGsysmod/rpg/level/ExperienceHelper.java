package com.RPGsys.RPGsysmod.rpg.level;

public class ExperienceHelper {
    public static int getKillExperience(int exp) {
        return (int) Math.floor(-3.75D + 2.5D * Math.sqrt(0.05D * exp + 2.25D) + 0.2D * exp);
    }
}
