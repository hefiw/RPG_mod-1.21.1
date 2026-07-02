package com.RPGsys.RPGsysmod.rpg.network;

import com.RPGsys.RPGsysmod.rpg.client.ClientRPGData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketHandler {
    public static void handle(
            SyncRPGDataPacket packet,
            IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            ClientRPGData.experience = packet.experience();
            ClientRPGData.abilityPoints = packet.ap();
            ClientRPGData.passiveSkillPoints = packet.sp();
        });
    }
}
