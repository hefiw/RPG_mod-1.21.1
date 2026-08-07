package com.RPGsys.RPGsysmod.rpg.item;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.network.SyncRPGDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;

public class SoulMirrorItem extends Item {
    public SoulMirrorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            PacketDistributor.sendToPlayer(
                    (ServerPlayer) player,
                    SyncRPGDataPacket.from(player)
            );
        }
        if (level.isClientSide && FMLEnvironment.dist == Dist.CLIENT) {
            openClientScreen();
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private static void openClientScreen() {
        try {
            Class.forName("com.RPGsys.RPGsysmod.rpg.client.gui.SoulMirrorClientHooks")
                    .getMethod("open")
                    .invoke(null);
        } catch (ReflectiveOperationException exception) {
            ExampleMod.LOGGER.error("Failed to open the Soul Mirror screen", exception);
        }
    }
}