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

public class DeathOverlay implements HudElement {
    public static final Identifier ID = Identifier.of(DungeonsBedwars.MOD_ID, "death_overlay");

    public static int countdown = 0;
    public static boolean running = false;
    public static float frame = 0;

    private static final float MAX_FRAME = 200f;
    private static final int WIDTH = 960 / 2;
    private static final int HEIGHT = 540 / 2;

    private static final Identifier RED_VIGNETTE = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/vignette/red");
    private static final Identifier GRAY_VIGNETTE = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/vignette/gray");

    @Environment(EnvType.CLIENT)
    public static void display() {
        running = true;
        frame = 0;
        countdown = 3;
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.of(DungeonsBedwars.MOD_ID, "death")), 1, 1));
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!running) return;

        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        int x = (screenWidth - WIDTH) / 2;
        int y = (screenHeight - HEIGHT) / 2;

        Identifier backTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/bottom/" + ((int) frame % 41));
        Identifier frontTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/top/" + (frame >=95?94:(int)frame));

        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, RED_VIGNETTE, 0, 0, screenWidth, screenHeight, new Color(1, 1, 1, 0.6f).getRGB());

        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, GRAY_VIGNETTE, 0, 0, screenWidth, screenHeight, new Color(1, 1, 1, 0.4f * (frame < 10 ? frame / 10f : 1)).getRGB());

        if (frame > 25) {
            Identifier countTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/count/" + (frame<29?3:countdown));
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, countTexture, x,y,WIDTH,HEIGHT,new Color(1,1,1,frame<29?(frame-25)/4f:1).getRGB());
        }

        if (frame > 19) context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, backTexture, x,y, WIDTH, HEIGHT);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, frontTexture,x,y, WIDTH, HEIGHT);

        if (!MinecraftClient.getInstance().isPaused()) frame += tickCounter.getDynamicDeltaTicks();
        if (frame > MAX_FRAME) running = false;
    }
}
