package com.RPGsys.RPGsysmod.rpg.passive.effects;

import com.RPGsys.RPGsysmod.rpg.passive.PassiveEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SpeedBoostPassive implements PassiveEffect {

    private static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("rpgsys", "speed_boost");

    @Override
    public void apply(LivingEntity entity, int level) {

        AttributeInstance attribute =
                entity.getAttribute(Attributes.MOVEMENT_SPEED);

        if (attribute == null)
            return;

        attribute.removeModifier(ID);

        double bonus = level * 0.5;

        attribute.addPermanentModifier(
                new AttributeModifier(
                        ID,
                        bonus,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                )
        );
    }
}