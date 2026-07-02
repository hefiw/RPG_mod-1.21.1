package com.RPGsys.RPGsysmod.rpg.event;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import com.RPGsys.RPGsysmod.rpg.level.ExperienceHelper;
import com.RPGsys.RPGsysmod.rpg.level.LevelHelper;
import com.RPGsys.RPGsysmod.rpg.level.LevelUpHelper;
import com.RPGsys.RPGsysmod.rpg.network.SyncRPGDataPacket;
import com.RPGsys.RPGsysmod.rpg.util.EntityUtil;
import com.RPGsys.RPGsysmod.rpg.util.RPGHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class EntityDeathHandler {
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Entity killer = victim.getKillCredit();

        if (victim instanceof Player && killer instanceof Player) {return;}
        if (!(killer instanceof LivingEntity  mobKiller)) {return;}
        if (killer instanceof Player && EntityUtil.isTamed(victim)) {return;}

        if (victim instanceof Player player && killer instanceof Mob mob) {
            RPGData victimData = RPGHelper.getData(player);
            RPGData killerData = RPGHelper.getData(mob);
            int gainedExp = victimData.getExperience() / 2;
            killerData.addExperience(gainedExp);
            EntityNameHandler.updateMobName(mob);
            return;
        }

        int victimExp = RPGHelper.getData(victim).getExperience();
        int gainedExp = ExperienceHelper.getKillExperience(victimExp);
        RPGData killerData = RPGHelper.getData(mobKiller);
        killerData.addExperience(gainedExp);
        if (mobKiller instanceof ServerPlayer serverPlayer) {
            LevelUpHelper.handleLevelUp(killerData, gainedExp, killerData.getExperience());
            System.out.println(
                    "SERVER SENT XP = "
                            + killerData.getExperience()
            );
            System.out.println(
                    "SERVER SENT AP = "
                            + killerData.getAbilityPoints()
            );
            System.out.println(
                    "SERVER SENT SP = "
                            + killerData.getPassiveSkillPoints()
            );
            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new SyncRPGDataPacket(
                            killerData.getExperience(),
                            killerData.getAbilityPoints(),
                            killerData.getPassiveSkillPoints()
                    )
            );
        }
        if (killer instanceof Mob mob) {
            EntityNameHandler.updateMobName(mob);
        }
    }
}
