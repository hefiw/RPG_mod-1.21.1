package com.RPGsys.RPGsysmod.rpg.passive.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ArmorBoostPassive implements PassiveEffect {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("rpgsys", "armor_boost");

    @Override
    public void apply(LivingEntity entity, int level) {
        AttributeInstance attribute = entity.getAttribute(Attributes.ARMOR);
        if (attribute == null) {return;}

        attribute.removeModifier(ID);
        attribute.addPermanentModifier(
                new AttributeModifier(
                        ID,
                        level * 35.0,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }
}
