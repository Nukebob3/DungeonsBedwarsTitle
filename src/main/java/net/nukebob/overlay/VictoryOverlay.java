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

public class VictoryOverlay {
    public static boolean running = false;

    @Environment(EnvType.CLIENT)
    public static void displayImage(PlayerEntity player) {
        AtomicBoolean lRunning = new AtomicBoolean(true);
        final int frames = 57;
        final float[] frame = {0};
        player.playSoundToPlayer(ModSounds.VICTORY, SoundCategory.MASTER, 1, 1);
        HudRenderCallback.EVENT.register((drawContext, tickDeltaManager) -> {
            if (lRunning.get()) {
                MinecraftClient client = MinecraftClient.getInstance();
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();
                Identifier texture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/victory/" + (int) Math.floor(frame[0]) + ".png");
                int width = 576;
                int height = 266;
                if (frame[0] > frames) {
                    if (frame[0] < frames + 100) {
                        texture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/victory/" + frames + ".png");
                    } else if (frame[0] < frames + 100 + 19) {
                        texture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/victory/" + "f" + (int) (frame[0] - frames - 100) + ".png");
                    } else {
                        texture = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/victory/" + 0 + ".png");
                    }
                }
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                if (frame[0] < frames + 100 + 19) drawContext.drawTexture(texture, (screenWidth - width) / 2, (screenHeight - height) / 2, 0, 0, width, height, width, height);
                RenderSystem.disableBlend();
                frame[0] += tickDeltaManager.getLastFrameDuration();
            }
            if (!(frame[0] < frames + 100 + 19)) {
                running = false;
                lRunning.set(false);
            }
        });
    }
}
