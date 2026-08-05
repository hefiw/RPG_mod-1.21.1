package com.RPGsys.RPGsysmod.rpg.client;

import java.util.HashMap;
import java.util.Map;

public class ClientRPGData {
    public static int experience = 0;
    public static int passiveSkillPoints = 0;
    public static int abilityPoints = 0;
    public static final Map<String, Integer> passiveLevels = new HashMap<>();

    public static int getPassiveLevel(String id) {
        return passiveLevels.getOrDefault(id, 0);
    }
}
