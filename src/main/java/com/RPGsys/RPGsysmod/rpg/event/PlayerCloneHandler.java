package com.RPGsys.RPGsysmod.rpg.event;

import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import com.RPGsys.RPGsysmod.rpg.level.LevelHelper;
import com.RPGsys.RPGsysmod.rpg.network.SyncRPGDataPacket;
import com.RPGsys.RPGsysmod.rpg.util.RPGHelper;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlayerCloneHandler {
    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        RPGData data = RPGHelper.getData(player);

        if (data.getLevel() == 0 && data.getExperience() == 0) {
            data.setExperience(
                    LevelHelper.getExperienceForLevel(1)
            );
        }

        PacketDistributor.sendToPlayer(
                player,
                new SyncRPGDataPacket(
                        data.getExperience(),
                        data.getAbilityPoints(),
                        data.getPassiveSkillPoints()
                )
        );
    }
}
