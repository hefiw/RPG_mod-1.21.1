package com.RPGsys.RPGsysmod.rpg.client.gui;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.client.ClientRPGData;
import com.RPGsys.RPGsysmod.rpg.network.UpgradePassivePacket;
import com.RPGsys.RPGsysmod.rpg.passive.PassiveSkillDefinition;
import com.RPGsys.RPGsysmod.rpg.passive.PlayerPassiveRegistry;
import com.RPGsys.RPGsysmod.rpg.passive.RacePassiveManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.RPGsys.RPGsysmod.rpg.passive.RacePassiveManager.getSkillsForRace;

public class SoulMirrorScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/soul_mirror_background.png"
    );
    private static final ResourceLocation FRAME_TOP = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/skill-frame-top.png"
    );
    private static final ResourceLocation FRAME_BOTTOM = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/skill-frame-bottom.png"
    );
    private static final ResourceLocation FRAME_LEFT = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/skill-frame-left.png"
    );
    private static final ResourceLocation FRAME_RIGHT = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/skill-frame-right.png"
    );
    private static final ResourceLocation ATTRIBUTE_SLOT = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/attribute-slot.png"
    );
    private static final ResourceLocation ATTRIBUTE_ITEM_SLOT = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/skill-slot.png"
    );
    private static final ResourceLocation SKILL_UP_BTN = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/skill-plus.png"
    );
    private static final int VIEWPORT_SIZE = 200;
    private static final int BACKGROUND_TEXTURE_SIZE = 512;
    private static final int DRAG_MOUSE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT;
    private static final double MIN_BACKGROUND_SCALE = (double) VIEWPORT_SIZE / BACKGROUND_TEXTURE_SIZE;
    private static final double MAX_BACKGROUND_SCALE = 1.0D;
    private static final double ZOOM_STEP = 0.1D;
    private static final int SLOT_WIDTH = 100;
    private static final int SLOT_HEIGHT = 33;

    private double backgroundX;
    private double backgroundY;
    private double backgroundScale = MAX_BACKGROUND_SCALE;
    private boolean draggingBackground;
    private double lastDragMouseX;
    private double lastDragMouseY;

    public SoulMirrorScreen() {
        super(Component.translatable("screen.rpgsys.soul_mirror"));
    }

    private void centerBackground() {
        int scaledSize = getScaledBackgroundSize();
        backgroundX = (VIEWPORT_SIZE - scaledSize) / 2.0D;
        backgroundY = (VIEWPORT_SIZE - scaledSize) / 2.0D;
    }

    private void clampBackgroundPosition() {
        int scaledSize = getScaledBackgroundSize();
        if (scaledSize <= VIEWPORT_SIZE) {
            backgroundX = (VIEWPORT_SIZE - scaledSize) / 2.0D;
            backgroundY = (VIEWPORT_SIZE - scaledSize) / 2.0D;
            return;
        }
        double minX = VIEWPORT_SIZE - scaledSize;
        double minY = VIEWPORT_SIZE - scaledSize;

        backgroundX = Mth.clamp(backgroundX, minX, 0);
        backgroundY = Mth.clamp(backgroundY, minY, 0);
    }

    @Override
    protected void init() {
        super.init();
        backgroundScale = MAX_BACKGROUND_SCALE;
        centerBackground();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        updateRightMouseDrag(mouseX, mouseY);

        int left = getViewportLeft();
        int top = getViewportTop();
        int scaledBackgroundSize = getScaledBackgroundSize();

        guiGraphics.enableScissor(left, top, left + VIEWPORT_SIZE, top + VIEWPORT_SIZE);
        guiGraphics.blit(
                BACKGROUND,
                left + Mth.floor(backgroundX),
                top + Mth.floor(backgroundY),
                scaledBackgroundSize,
                scaledBackgroundSize,
                0.0F,
                0.0F,
                BACKGROUND_TEXTURE_SIZE,
                BACKGROUND_TEXTURE_SIZE,
                BACKGROUND_TEXTURE_SIZE,
                BACKGROUND_TEXTURE_SIZE
        );
        guiGraphics.disableScissor();

        guiGraphics.blit(
                FRAME_TOP,
                left,
                top-5,
                VIEWPORT_SIZE,
                15,
                0.0F,
                0.0F,
                512,
                38,
                512,
                38
        );
        guiGraphics.blit(
                FRAME_BOTTOM,
                left,
                top+VIEWPORT_SIZE-10,
                VIEWPORT_SIZE,
                15,
                0.0F,
                0.0F,
                512,
                38,
                512,
                38
        );

        guiGraphics.blit(
                FRAME_LEFT,
                left-4,
                top-2,
                24,
                VIEWPORT_SIZE+4,
                0.0F,
                0.0F,
                61,
                512,
                61,
                512
        );
        guiGraphics.blit(
                FRAME_RIGHT,
                left+VIEWPORT_SIZE-20,
                top-2,
                24,
                VIEWPORT_SIZE+4,
                0.0F,
                0.0F,
                61,
                512,
                61,
                512
        );
        guiGraphics.drawString(
                font,
                "PSP: " + ClientRPGData.passiveSkillPoints,
                left + VIEWPORT_SIZE - 50,
                top + 8,
                0xFFFFFF,
                true
        );

        guiGraphics.drawString(
                font,
                "ASP: " + ClientRPGData.abilityPoints,
                left + VIEWPORT_SIZE - 50,
                top + 20,
                0xFFFFFF,
                true
        );

        renderPassiveSlots(guiGraphics, mouseX, mouseY);
    }

    private void renderPassiveSlots(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Set<String> availableSkills = RacePassiveManager.getSkillsForRace(ClientRPGData.race);
        List<PassiveSkillDefinition> skills =
                PlayerPassiveRegistry.SKILLS
                        .values()
                        .stream()
                        .filter(skill -> availableSkills.contains(skill.id()))
                        .toList();
        int centerX = getViewportLeft();
        int centerY = getViewportTop();
        int leftCount = (skills.size() + 1) / 2;

        for (int i = 0; i < skills.size(); i++) {
            PassiveSkillDefinition skill = skills.get(i);
            boolean leftSide = i < leftCount;
            int slotX;
            int slotY;
            if (leftSide) {
                slotX = centerX - SLOT_WIDTH-2;
                slotY = centerY + i * (SLOT_HEIGHT+5);
            } else {
                slotX = centerX + VIEWPORT_SIZE+2;
                slotY = centerY + (i - leftCount) * (SLOT_HEIGHT+5);
            }
            slotY+=10;

            renderPassiveSlot(
                    guiGraphics,
                    slotX,
                    slotY,
                    skill,
                    mouseX,
                    mouseY,
                    leftSide
            );
        }
    }

    private void renderPassiveSlot(GuiGraphics guiGraphics, int x, int y, PassiveSkillDefinition skill, int mouseX, int mouseY, boolean left) {
        guiGraphics.blit(
                ATTRIBUTE_SLOT,
                x,
                y,
                SLOT_WIDTH,
                SLOT_HEIGHT,
                0.0F,
                0.0F,
                128,
                48,
                128,
                48
        );
        guiGraphics.blit(
                skill.icon(),
                left ? x+6 : x+SLOT_WIDTH-26,
                y+6,
                20,
                20,      // размер на экране
                0.0F,
                0.0F,
                32,
                32,      // размер области текстуры
                32,
                32       // полный размер текстуры
        );
        guiGraphics.blit(
                ATTRIBUTE_ITEM_SLOT,
                left ? x+6 : x+SLOT_WIDTH-26,
                y+6,
                20,
                20,      // размер на экране
                0.0F,
                0.0F,
                32,
                32,      // размер области текстуры
                32,
                32       // полный размер текстуры
        );

        boolean inside = mouseX >= x && mouseX <= x + SLOT_WIDTH && mouseY >= y && mouseY <= y + SLOT_HEIGHT;

        guiGraphics.blit(
                SKILL_UP_BTN,
                left ? x+SLOT_WIDTH-16 : x+6,
                y+11,
                10,
                10,      // размер на экране
                inside ? 32.0F : 0.0F,
                0.0F,
                32,
                32,      // размер области текстуры
                64,
                32       // полный размер текстуры
        );

        int level = ClientRPGData.getPassiveLevel(skill.id());
        guiGraphics.drawString(
                font,
                String.valueOf(level),
                left ? x+40 : x+SLOT_WIDTH-46,
                y + 11,
                0xFFFFFF
        );
        if (inside) {
            List<Component> tooltip = List.of(
                            Component.literal(skill.name()),
                            Component.literal(skill.description()),
                            Component.literal("Уровень: " + level));

            guiGraphics.renderTooltip(
                    font,
                    tooltip,
                    Optional.empty(),
                    mouseX,
                    mouseY
            );
        }
    }

    private void tryUpgradeSkill(double mouseX, double mouseY) {
        Set<String> availableSkills =
                RacePassiveManager.getSkillsForRace(ClientRPGData.race);

        List<PassiveSkillDefinition> skills =
                PlayerPassiveRegistry.SKILLS
                        .values()
                        .stream()
                        .filter(skill -> availableSkills.contains(skill.id()))
                        .toList();

        int centerX = getViewportLeft();
        int centerY = getViewportTop();
        int leftCount = (skills.size() + 1) / 2;

        for (int i = 0; i < skills.size(); i++) {

            PassiveSkillDefinition skill = skills.get(i);
            boolean leftSide = i < leftCount;

            int slotX;
            int slotY;

            if (leftSide) {
                slotX = centerX - SLOT_WIDTH - 2;
                slotY = centerY + i * (SLOT_HEIGHT + 5);
            } else {
                slotX = centerX + VIEWPORT_SIZE + 2;
                slotY = centerY + (i - leftCount) * (SLOT_HEIGHT + 5);
            }

            slotY += 10;
            int plusX = leftSide ? slotX + SLOT_WIDTH - 16 : slotX + 6;

            int plusY = slotY + 11;

            if (mouseX >= plusX
                    && mouseX <= plusX + 10
                    && mouseY >= plusY
                    && mouseY <= plusY + 10
            ) {
                if (ClientRPGData.passiveSkillPoints <= 0) {return;}

                PacketDistributor.sendToServer(new UpgradePassivePacket(skill.id()));
                ClientRPGData.passiveLevels.merge(
                        skill.id(),
                        1,
                        Integer::sum
                );
                ClientRPGData.passiveSkillPoints--;
                return;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            tryUpgradeSkill(mouseX, mouseY);
            if (isMouseOverViewport(mouseX, mouseY)) {
                startDraggingBackground(mouseX, mouseY);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == DRAG_MOUSE_BUTTON && draggingBackground) {
            moveBackgroundBy(dragX, dragY);
            lastDragMouseX = mouseX;
            lastDragMouseY = mouseY;
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == DRAG_MOUSE_BUTTON && draggingBackground) {
            stopDraggingBackground();
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!isMouseOverViewport(mouseX, mouseY)) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        double previousScale = backgroundScale;
        backgroundScale = Mth.clamp(
                backgroundScale + scrollY * ZOOM_STEP,
                MIN_BACKGROUND_SCALE,
                MAX_BACKGROUND_SCALE
        );

        if (backgroundScale != previousScale) {
            double viewportMouseX = mouseX - getViewportLeft();
            double viewportMouseY = mouseY - getViewportTop();
            double scaleRatio = backgroundScale / previousScale;
            backgroundX = viewportMouseX - (viewportMouseX - backgroundX) * scaleRatio;
            backgroundY = viewportMouseY - (viewportMouseY - backgroundY) * scaleRatio;
        }
        clampBackgroundPosition();
        return true;
    }

    @Override
    public void removed() {
        stopDraggingBackground();
        super.removed();
    }

    private void updateRightMouseDrag(double mouseX, double mouseY) {
        if (!draggingBackground) {
            return;
        }

        long window = Minecraft.getInstance().getWindow().getWindow();
        if (GLFW.glfwGetMouseButton(window, DRAG_MOUSE_BUTTON) == GLFW.GLFW_PRESS) {
            double dragX = mouseX - lastDragMouseX;
            double dragY = mouseY - lastDragMouseY;
            if (dragX != 0.0D || dragY != 0.0D) {
                moveBackgroundBy(dragX, dragY);
                lastDragMouseX = mouseX;
                lastDragMouseY = mouseY;
            }
            return;
        }

        stopDraggingBackground();
    }

    private void startDraggingBackground(double mouseX, double mouseY) {
        draggingBackground = true;
        lastDragMouseX = mouseX;
        lastDragMouseY = mouseY;
    }

    private void stopDraggingBackground() {
        draggingBackground = false;
    }

    private void moveBackgroundBy(double dragX, double dragY) {
        backgroundX += dragX;
        backgroundY += dragY;
        clampBackgroundPosition();
    }

    private boolean isMouseOverViewport(double mouseX, double mouseY) {
        int left = getViewportLeft();
        int top = getViewportTop();
        return mouseX >= left && mouseX < left + VIEWPORT_SIZE && mouseY >= top && mouseY < top + VIEWPORT_SIZE;
    }

    private int getViewportLeft() {
        return (width - VIEWPORT_SIZE) / 2;
    }

    private int getViewportTop() {
        return (height - VIEWPORT_SIZE) / 2;
    }

    private int getScaledBackgroundSize() {
        return Mth.floor(BACKGROUND_TEXTURE_SIZE * backgroundScale);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}