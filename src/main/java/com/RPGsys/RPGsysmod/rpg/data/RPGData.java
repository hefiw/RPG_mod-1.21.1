package com.RPGsys.RPGsysmod.rpg.data;

import com.RPGsys.RPGsysmod.rpg.level.LevelHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class RPGData implements INBTSerializable<CompoundTag> {
    private int experience;
    private boolean initialized;
    private int passiveSkillPoints;
    private int abilityPoints;

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

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("Experience", experience);
        tag.putBoolean("Initialized", initialized);
        tag.putInt("PassiveSkillPoints", passiveSkillPoints);
        tag.putInt("AbilityPoints", abilityPoints);

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
    }
}
