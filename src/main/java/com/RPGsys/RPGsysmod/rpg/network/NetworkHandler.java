package com.RPGsys.RPGsysmod.rpg.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                SyncRPGDataPacket.TYPE,
                SyncRPGDataPacket.STREAM_CODEC,
                ClientPacketHandler::handle
        );
    }
}
