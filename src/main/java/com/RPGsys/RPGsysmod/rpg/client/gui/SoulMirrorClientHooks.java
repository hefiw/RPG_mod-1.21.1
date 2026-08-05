package com.RPGsys.RPGsysmod.rpg.client.gui;

import net.minecraft.client.Minecraft;

public class SoulMirrorClientHooks {
    public static void open() {
        Minecraft.getInstance().setScreen(new SoulMirrorScreen());
    }
}