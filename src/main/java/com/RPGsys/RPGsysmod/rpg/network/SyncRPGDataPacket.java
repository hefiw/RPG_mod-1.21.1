package com.RPGsys.RPGsysmod.rpg.network;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.attachment.ModAttachments;
import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import com.RPGsys.RPGsysmod.rpg.race.PlayerRaceHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record SyncRPGDataPacket(int experience, int ap, int sp, Map<String, Integer> passiveLevels, String race) implements CustomPacketPayload {
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
                        buf.writeUtf(packet.race);
                    },
                    buf -> new SyncRPGDataPacket(
                            buf.readInt(),
                            buf.readInt(),
                            buf.readInt(),
                            buf.readMap(HashMap::new, FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt),
                            buf.readUtf()
                    )
            );

    public static SyncRPGDataPacket from(Player player) {
        RPGData data = player.getData(ModAttachments.RPG_DATA);
        return new SyncRPGDataPacket(
                data.getExperience(),
                data.getAbilityPoints(),
                data.getPassiveSkillPoints(),
                data.getPassiveLevels(),
                PlayerRaceHelper.getRace(player)
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
