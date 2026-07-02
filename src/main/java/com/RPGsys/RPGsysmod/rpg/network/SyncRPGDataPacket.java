package com.RPGsys.RPGsysmod.rpg.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncRPGDataPacket(int level, int experience) implements CustomPacketPayload {
    public static final Type<SyncRPGDataPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    "rpgsys",
                    "sync_rpg_data"
            ));

    public static final StreamCodec<FriendlyByteBuf, SyncRPGDataPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> {
                        buf.writeInt(packet.level);
                        buf.writeInt(packet.experience);
                    },
                    buf -> new SyncRPGDataPacket(
                            buf.readInt(),
                            buf.readInt()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
