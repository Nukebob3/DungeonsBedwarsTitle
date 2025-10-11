package net.nukebob.overlay;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.nukebob.DungeonsBedwars;

import java.awt.*;

public class VictoryOverlay implements HudElement {
    public static final Identifier ID = Identifier.of(DungeonsBedwars.MOD_ID, "victory_overlay");

    private static final int INTRO_FRAMES = 57;
    private static final int HOLD_FRAMES = 80;
    private static final int FADE_FRAMES = 18;

    private static final int WIDTH = 576;
    private static final int HEIGHT = 266;

    public static boolean running = false;
    public static float frame = 0;

    @Environment(EnvType.CLIENT)
    public static void display() {
        running = true;
        frame = 0;
        MinecraftClient.getInstance().getSoundManager().play(
                PositionedSoundInstance.master(
                        SoundEvent.of(Identifier.of(DungeonsBedwars.MOD_ID, "finally")), 1, 1
                )
        );
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!running) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        if (frame <= INTRO_FRAMES) {
            Identifier texture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/victory/" + (int) Math.floor(frame));
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, (screenWidth - WIDTH) / 2, (screenHeight - HEIGHT) / 2, WIDTH, HEIGHT);
        } else if (frame <= INTRO_FRAMES + HOLD_FRAMES) {
            Identifier texture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/victory/" + 57);
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, (screenWidth - WIDTH) / 2, (screenHeight - HEIGHT) / 2, WIDTH, HEIGHT);
        } else if (frame <= INTRO_FRAMES + HOLD_FRAMES + FADE_FRAMES) {
            Identifier texture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/victory/" + 57);
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, (screenWidth - WIDTH) / 2, (screenHeight - HEIGHT) / 2, WIDTH, HEIGHT, new Color(1, 1, 1, (18 - (frame - 57 - 80)) / 18).getRGB());
        }

        if (frame <= 176) {
            if (!client.isPaused()) frame += tickCounter.getDynamicDeltaTicks();
        }
        else running = false;
    }
}
