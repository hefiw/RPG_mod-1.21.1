package com.RPGsys.RPGsysmod.rpg.network;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.passive.PlayerPassiveRegistry;
import com.RPGsys.RPGsysmod.rpg.util.RPGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpgradePassivePacket(String skillId) implements CustomPacketPayload {
    public static final Type<UpgradePassivePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "upgrade_passive"));

    public static final StreamCodec<FriendlyByteBuf, UpgradePassivePacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.skillId),
                    buf -> new UpgradePassivePacket(buf.readUtf())
            );

    public static void handle(UpgradePassivePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            if (!PlayerPassiveRegistry.SKILLS.containsKey(packet.skillId)) {
                return;
            }

            var data = RPGHelper.getData(player);
            if (!data.spendPassivePoint(1)) {
                return;
            }

            data.levelUpPassive(packet.skillId);
            PacketDistributor.sendToPlayer(
                    player,
                    SyncRPGDataPacket.from(data)
            );
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}