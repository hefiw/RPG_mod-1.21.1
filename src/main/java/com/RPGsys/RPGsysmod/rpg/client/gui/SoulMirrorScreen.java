package com.RPGsys.RPGsysmod.rpg.client.gui;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.client.ClientRPGData;
import com.RPGsys.RPGsysmod.rpg.network.UpgradePassivePacket;
import com.RPGsys.RPGsysmod.rpg.passive.PassiveSkillDefinition;
import com.RPGsys.RPGsysmod.rpg.passive.PlayerPassiveRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class SoulMirrorScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/soul_mirror_background.jpg"
    );
    private static final int PANEL_SIZE = 256;
    private final List<PassiveSkillDefinition> skills = new ArrayList<>();

    public SoulMirrorScreen() {
        super(Component.translatable("screen.rpgsys.soul_mirror"));
    }

    @Override
    protected void init() {
        PlayerPassiveRegistry.init();
        skills.clear();
        skills.addAll(PlayerPassiveRegistry.SKILLS.values());
        //rebuildSkillButtons();
    }

    private void rebuildSkillButtons() {
        clearWidgets();
        int startX = (width - PANEL_SIZE) / 2 + 24;
        int startY = (height - PANEL_SIZE) / 2 + 44;
        int columns = 2;

        for (int i = 0; i < skills.size(); i++) {
            PassiveSkillDefinition skill = skills.get(i);
            int x = startX + (i % columns) * 104;
            int y = startY + (i / columns) * 52;
            addRenderableWidget(Button.builder(
                            Component.literal("+"),
                            button -> PacketDistributor.sendToServer(new UpgradePassivePacket(skill.id()))
                    )
                    .bounds(x + 76, y + 14, 20, 20)
                    .build());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int left = (width - PANEL_SIZE) / 2;
        int top = (height - PANEL_SIZE) / 2;

        guiGraphics.blit(BACKGROUND, left, top, 0, 0, PANEL_SIZE, PANEL_SIZE, PANEL_SIZE, PANEL_SIZE);
        guiGraphics.drawCenteredString(font, title, width / 2, top + 14, 0xFFE7C27D);
        guiGraphics.drawString(font, Component.literal("Очки пассивных навыков: " + ClientRPGData.passiveSkillPoints), left + 18, top + 28, 0xFFE7C27D, false);
        //renderSkills(guiGraphics, left + 24, top + 44, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderSkills(GuiGraphics guiGraphics, int startX, int startY, int mouseX, int mouseY) {
        for (int i = 0; i < skills.size(); i++) {
            PassiveSkillDefinition skill = skills.get(i);
            int x = startX + (i % 2) * 104;
            int y = startY + (i / 2) * 52;
            int level = ClientRPGData.getPassiveLevel(skill.id());

            guiGraphics.blit(skill.icon(), x, y, 0, 0, 24, 24, 24, 24);
            guiGraphics.drawString(font, skill.name(), x + 30, y, 0xFFFFFFFF, false);
            guiGraphics.drawString(font, Component.literal("Ур. " + level), x + 30, y + 11, 0xFFB9A3FF, false);

            if (mouseX >= x && mouseX <= x + 96 && mouseY >= y && mouseY <= y + 34) {
                guiGraphics.renderTooltip(font, List.of(
                        FormattedCharSequence.forward(skill.name(), net.minecraft.network.chat.Style.EMPTY),
                        FormattedCharSequence.forward(skill.description(), net.minecraft.network.chat.Style.EMPTY),
                        FormattedCharSequence.forward("Стоимость: 1 пассивное очко", net.minecraft.network.chat.Style.EMPTY)
                ), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}