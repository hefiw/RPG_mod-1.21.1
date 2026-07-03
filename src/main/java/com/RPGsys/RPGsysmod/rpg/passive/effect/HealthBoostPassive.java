package com.RPGsys.RPGsysmod.rpg.passive.effect;

import com.RPGsys.RPGsysmod.rpg.passive.MobPassiveType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class HealthBoostPassive implements PassiveEffect {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("rpgsys", "health_boost");

    @Override
    public void apply(LivingEntity entity, int level) {
        AttributeInstance attribute = entity.getAttribute(Attributes.MAX_HEALTH);
        if (attribute == null) {
            return;
        }
        double oldMaxHealth = attribute.getValue();

        attribute.removeModifier(ID);

        attribute.addPermanentModifier(
                new AttributeModifier(
                        ID,
                        0.25 * level,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                )
        );

        double dif = attribute.getValue() - oldMaxHealth;
        if (dif > 0) {
            entity.setHealth(
                    Math.min(
                            entity.getHealth() + (float) dif,
                            (float) attribute.getValue()
                    )
            );
        }
    }
}
