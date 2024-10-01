package net.nukebob.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.nukebob.DungeonsBedwars;
import net.nukebob.sound.ModSounds;

import java.util.concurrent.atomic.AtomicBoolean;

public class DeathOverlay {
    public static int countdown;
    public static boolean running = false;

    @Environment(EnvType.CLIENT)
    public static void displayImage(PlayerEntity player) {
        countdown = 3;
        AtomicBoolean lRunning = new AtomicBoolean(true);
        final int frames = 95;
        final int backFrames = 41;
        final float[] frame = {0};
        final float[] backFrame = {0};
        final float[] countFrame = {0};
        player.playSoundToPlayer(ModSounds.DEATH, SoundCategory.MASTER, 1, 1);
        HudRenderCallback.EVENT.register((drawContext, tickDeltaManager) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();
            int width = 960 / 2;
            int height = 540 / 2;
            if (lRunning.get()) {
                Identifier frontTexture;
                Identifier backTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/bottom/" + ((int) Math.floor(backFrame[0]) % backFrames) + ".png");
                if (Math.floor(frame[0]) >= frames) {
                    frontTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/top/" + (frames - 1) + ".png");
                } else if (lRunning.get()) {
                    frontTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/top/" + (int) Math.floor(frame[0]) + ".png");
                } else {
                    frontTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/top/" + 0 + ".png");
                }

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                Identifier redVignette = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/vignette/red.png");
                drawContext.setShaderColor(1.0F, 1.0F, 1.0F, 0.6f);
                drawContext.drawTexture(redVignette, 0, 0, -90, 0.0F, 0.0F, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight(), drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight());
                Identifier grayVignette = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/vignette/gray.png");
                float opacity = 0.4f;
                if (frame[0] < 10) {
                    drawContext.setShaderColor(1.0F, 1.0F, 1.0F, (frame[0] / 10 * opacity));
                } else {
                    drawContext.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
                }
                drawContext.drawTexture(grayVignette, 0, 0, -91, 0.0F, 0.0F, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight(), drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight());
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                drawContext.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                if (Math.floor(frame[0]) > 25) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    Identifier countTexture;
                    if (Math.floor(countFrame[0]) < 4) {
                        countTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/count/" + (int) Math.floor(countFrame[0]) + ".png");
                        drawContext.drawTexture(countTexture, (screenWidth - width) / 2, (screenHeight - height) / 2, 0, 0, width, height, width, height);
                    } else {
                        countTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/count/" + "c" + countdown + ".png");
                        drawContext.drawTexture(countTexture, (screenWidth - width) / 2, (screenHeight - height) / 2, 0, 0, width, height, width, height);
                    }
                    drawContext.drawTexture(frontTexture, (screenWidth - width) / 2, (screenHeight - height) / 2, 0, 0, width, height, width, height);
                    RenderSystem.disableBlend();
                    countFrame[0] += tickDeltaManager.getLastFrameDuration();
                } else {
                    Identifier countTexture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/death/count/" + "0" + ".png");
                    drawContext.drawTexture(countTexture, (screenWidth - width) / 2, (screenHeight - height) / 2, 0, 0, width, height, width, height);
                }
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                if (Math.floor(frame[0]) < frames && Math.floor(frame[0]) > 19)
                    drawContext.drawTexture(backTexture, (screenWidth - width) / 2, (screenHeight - height) / 2, 0, 0, width, height, width, height);
                drawContext.drawTexture(frontTexture, (screenWidth - width) / 2, (screenHeight - height) / 2, 0, 0, width, height, width, height);
                RenderSystem.disableBlend();
                backFrame[0] += tickDeltaManager.getLastFrameDuration();
                frame[0] += tickDeltaManager.getLastFrameDuration();
            }
            if (frame[0] > 500) {
                DeathOverlay.running = false;
            }
            if (!running) {
                lRunning.set(false);
                frame[0] = 0;
                backFrame[0] = 0;
                countFrame[0] = 0;
            }
        });
    }


}
