package com.RPGsys.RPGsysmod.rpg.passive;

import com.RPGsys.RPGsysmod.rpg.util.RPGHelper;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class PlayerPassiveEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {return;}
        int regenerationLevel = RPGHelper.getData(player).getPassiveLevel("regeneration");

        if (regenerationLevel <= 0) {return;}
        // раз в секунду
        if (player.tickCount % 80 != 0) {return;}

        float healAmount = regenerationLevel * 0.05F;
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(healAmount);
        }
    }
}