package com.RPGsys.RPGsysmod.rpg.client;

import com.RPGsys.RPGsysmod.rpg.level.LevelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

public class RPGHudOverlay {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiLayerEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();

        Player player = mc.player;

        if (player == null) {
            return;
        }

        GuiGraphics gui = event.getGuiGraphics();

        int exp = ClientRPGData.experience;
        int sp = ClientRPGData.passiveSkillPoints;
        int ap = ClientRPGData.abilityPoints;
        int level = LevelHelper.getLevelFromExperience(exp);

        gui.drawString(
                mc.font,
                "Lvl: " + level,
                5,
                gui.guiHeight() - 45,
                0xFFFFFF
        );
        gui.drawString(
                mc.font,
                exp + "",
                5,
                gui.guiHeight() - 35,
                0xFFFFFF
        );
        gui.drawString(
                mc.font,
                ap + ": AP",
                5,
                gui.guiHeight() - 25,
                0xFFFFFF
        );
        gui.drawString(
                mc.font,
                sp + ": SP",
                5,
                gui.guiHeight() - 15,
                0xFFFFFF
        );
    }
}
