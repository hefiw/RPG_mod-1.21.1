package com.RPGsys.RPGsysmod.rpg.level;


import com.RPGsys.RPGsysmod.rpg.data.RPGData;

public class LevelUpHelper {
    public static void handleLevelUp(
            RPGData data,
            int gainExp,
            int newExp
    ) {
        int prevLvl = LevelHelper.getLevelFromExperience(newExp-gainExp);
        int newLvl = LevelHelper.getLevelFromExperience(newExp);
        for (int lvl = prevLvl + 1; lvl <= newLvl; lvl++) {

            if (lvl % 5 == 0) {
                data.addPassiveSkillPoints(1);
            }

            if (lvl % 20 == 0) {
                data.addAbilityPoints(1);
            }
        }
    }
}
