package com.RPGsys.RPGsysmod.rpg.data;

import com.RPGsys.RPGsysmod.rpg.level.LevelHelper;
import com.RPGsys.RPGsysmod.rpg.passive.MobPassiveType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.EnumMap;

public class RPGData implements INBTSerializable<CompoundTag> {
    private int experience;
    private boolean initialized;
    private int passiveSkillPoints;
    private int abilityPoints;
    private final EnumMap<MobPassiveType, Integer> passiveSkills = new EnumMap<>(MobPassiveType.class);

    public RPGData(int experience, boolean initialized,int passiveSkillPoints, int abilityPoints) {
        this.experience = experience;
        this.initialized = initialized;
        this.passiveSkillPoints = passiveSkillPoints;
        this.abilityPoints = abilityPoints;
    }

    public int getLevel() {
        return LevelHelper.getLevelFromExperience(experience);
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int amount) {
        experience += amount;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public int getPassiveSkillPoints() {
        return passiveSkillPoints;
    }

    public int getAbilityPoints() {
        return abilityPoints;
    }

    public void addPassiveSkillPoints(int amount) {
        passiveSkillPoints += amount;
    }

    public void addAbilityPoints(int amount) {
        abilityPoints += amount;
    }

    public boolean spendPassivePoint(int amount) {
        if (passiveSkillPoints < amount) {return false;}
        passiveSkillPoints-=amount;
        return true;
    }

    public boolean spendAbilityPoint(int amount) {
        if (abilityPoints < amount) {return false;}
        abilityPoints-=amount;
        return true;
    }

    public EnumMap<MobPassiveType, Integer> getPassiveSkills() {
        return passiveSkills;
    }

    public void addPassiveSkill(MobPassiveType passive) {
        passiveSkills.merge(passive, 1, Integer::sum);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("Experience", experience);
        tag.putBoolean("Initialized", initialized);
        tag.putInt("PassiveSkillPoints", passiveSkillPoints);
        tag.putInt("AbilityPoints", abilityPoints);
        CompoundTag passiveTag = new CompoundTag();

        for (var entry : passiveSkills.entrySet()) {
            passiveTag.putInt(entry.getKey().name(), entry.getValue());
        }

        tag.put("PassiveSkills", passiveTag);

        return tag;
    }

    @Override
    public void deserializeNBT(
            HolderLookup.Provider provider,
            CompoundTag tag
    ) {
        experience = tag.getInt("Experience");
        initialized = tag.getBoolean("Initialized");
        passiveSkillPoints = tag.getInt("PassiveSkillPoints");
        abilityPoints = tag.getInt("AbilityPoints");

        passiveSkills.clear();
        CompoundTag passiveTag = tag.getCompound("PassiveSkills");
        for (String key : passiveTag.getAllKeys()) {
            passiveSkills.put(MobPassiveType.valueOf(key), passiveTag.getInt(key));
        }
    }
}
