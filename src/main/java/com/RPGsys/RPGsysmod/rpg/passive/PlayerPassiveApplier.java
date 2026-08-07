package com.RPGsys.RPGsysmod.rpg.passive;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class PlayerPassiveApplier {

    public static void apply(Player player, RPGData data) {

        apply(
                player,
                Attributes.ATTACK_DAMAGE,
                "attack_damage",
                data.getPassiveLevel("attack_damage"),
                0.05
        );

        apply(
                player,
                Attributes.MAX_HEALTH,
                "health",
                data.getPassiveLevel("health"),
                2.0
        );

        apply(
                player,
                Attributes.MOVEMENT_SPEED,
                "speed",
                data.getPassiveLevel("speed"),
                0.02
        );

        apply(
                player,
                AttributeRegistry.MAX_MANA,
                "max_mana_count",
                data.getPassiveLevel("max_mana_count"),
                25.0
        );

        apply(
                player,
                AttributeRegistry.MANA_REGEN,
                "mana_regeneration",
                data.getPassiveLevel("mana_regeneration"),
                0.05
        );

        apply(
                player,
                AttributeRegistry.SPELL_POWER,
                "spell_damage",
                data.getPassiveLevel("spell_damage"),
                0.03
        );

        apply(
                player,
                AttributeRegistry.SPELL_RESIST,
                "magic_resistance",
                data.getPassiveLevel("magic_resistance"),
                0.02
        );

        applyResistance(
                player,
                data.getPassiveLevel("resistance")
        );
    }

    private static void apply(
            Player player,
            Holder<Attribute> attribute,
            String id,
            int level,
            double value
    ) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) {return;}

        ResourceLocation modifierId =
                ResourceLocation.fromNamespaceAndPath(
                        ExampleMod.MODID,
                        id
                );

        AttributeModifier old = instance.getModifier(modifierId);

        if (old != null) {instance.removeModifier(modifierId);}
        if (level <= 0) {return;}

        instance.addPermanentModifier(
                new AttributeModifier(
                        modifierId,
                        level * value,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
        if (attribute == Attributes.MAX_HEALTH) {player.heal(0.01F);}
        player.refreshDimensions();
    }

    private static void applyResistance(Player player, int level) {
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);

        if (armor == null) {return;}

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID,
                "resistance"
        );

        armor.removeModifier(id);
        if (level <= 0) {return;}

        armor.addPermanentModifier(
                new AttributeModifier(
                        id,
                        level * 1.0,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }
}