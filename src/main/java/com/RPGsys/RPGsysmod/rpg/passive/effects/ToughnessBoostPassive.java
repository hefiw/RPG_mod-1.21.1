package com.RPGsys.RPGsysmod.rpg.passive.effects;

import com.RPGsys.RPGsysmod.rpg.passive.PassiveEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ToughnessBoostPassive implements PassiveEffect {

    private static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("rpgsys", "toughness_boost");

    @Override
    public void apply(LivingEntity entity, int level) {

        AttributeInstance attribute =
                entity.getAttribute(Attributes.ARMOR_TOUGHNESS);

        if (attribute == null)
            return;

        attribute.removeModifier(ID);

        attribute.addPermanentModifier(
                new AttributeModifier(
                        ID,
                        level * 5.0,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }
}