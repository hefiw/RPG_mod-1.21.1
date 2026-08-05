package com.RPGsys.RPGsysmod.rpg.client.gui;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.client.ClientRPGData;
import com.RPGsys.RPGsysmod.rpg.network.UpgradePassivePacket;
import com.RPGsys.RPGsysmod.rpg.passive.PassiveSkillDefinition;
import com.RPGsys.RPGsysmod.rpg.passive.PlayerPassiveRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SoulMirrorScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/soul_mirror_background.png"
    );
    private static final ResourceLocation FRAME = ResourceLocation.fromNamespaceAndPath(
            ExampleMod.MODID,
            "textures/gui/skill-frame.png"
    );
    private static final int VIEWPORT_SIZE = 200;
    private static final int BACKGROUND_TEXTURE_SIZE = 512;
    private static final int DRAG_MOUSE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT;
    private static final double MIN_BACKGROUND_SCALE = (double) VIEWPORT_SIZE / BACKGROUND_TEXTURE_SIZE;
    private static final double MAX_BACKGROUND_SCALE = 1.0D;
    private static final double ZOOM_STEP = 0.1D;

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
                FRAME,
                left-2,
                top-2,
                VIEWPORT_SIZE+4,
                VIEWPORT_SIZE+4,
                0.0F,
                0.0F,
                BACKGROUND_TEXTURE_SIZE,
                BACKGROUND_TEXTURE_SIZE,
                BACKGROUND_TEXTURE_SIZE,
                BACKGROUND_TEXTURE_SIZE
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == DRAG_MOUSE_BUTTON && isMouseOverViewport(mouseX, mouseY)) {
            startDraggingBackground(mouseX, mouseY);
            return true;
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

    private class TransparentPlusButton extends AbstractButton {
        private final Consumer<AbstractButton> onPress;

        private TransparentPlusButton(int x, int y, int width, int height, Consumer<AbstractButton> onPress) {
            super(x, y, width, height, Component.literal("+"));
            this.onPress = onPress;
        }

        @Override
        public void onPress() {
            onPress.accept(this);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int color = isHoveredOrFocused() ? 0xFFFFFF55 : 0xFFFFFFFF;
            guiGraphics.drawCenteredString(
                    font,
                    getMessage(),
                    getX() + width / 2,
                    getY() + (height - 8) / 2,
                    color
            );
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            defaultButtonNarrationText(narrationElementOutput);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}