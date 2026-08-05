package com.RPGsys.RPGsysmod.rpg.network;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public record SyncRPGDataPacket(int experience, int ap, int sp, Map<String, Integer> passiveLevels) implements CustomPacketPayload {
    public static final Type<SyncRPGDataPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    ExampleMod.MODID,
                    "sync_rpg_data"
            ));

    public static final StreamCodec<FriendlyByteBuf, SyncRPGDataPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> {
                        buf.writeInt(packet.experience);
                        buf.writeInt(packet.ap);
                        buf.writeInt(packet.sp);
                        buf.writeMap(packet.passiveLevels, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
                    },
                    buf -> new SyncRPGDataPacket(
                            buf.readInt(),
                            buf.readInt(),
                            buf.readInt(),
                            buf.readMap(HashMap::new, FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt)
                    )
            );

    public static SyncRPGDataPacket from(RPGData data) {
        return new SyncRPGDataPacket(
                data.getExperience(),
                data.getAbilityPoints(),
                data.getPassiveSkillPoints(),
                data.getPassiveLevels()
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
