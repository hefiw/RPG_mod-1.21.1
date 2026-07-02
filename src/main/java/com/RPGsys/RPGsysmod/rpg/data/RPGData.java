package com.RPGsys.RPGsysmod.rpg.data;

import com.RPGsys.RPGsysmod.rpg.level.LevelHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class RPGData implements INBTSerializable<CompoundTag> {
    private int experience;
    private boolean initialized;

    public RPGData(int experience, boolean initialized) {
        this.experience = experience;
        this.initialized = initialized;
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

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("Experience", experience);
        tag.putBoolean("Initialized", initialized);

        return tag;
    }

    @Override
    public void deserializeNBT(
            HolderLookup.Provider provider,
            CompoundTag tag
    ) {
        experience = tag.getInt("Experience");
        initialized = tag.getBoolean("Initialized");
    }
}
