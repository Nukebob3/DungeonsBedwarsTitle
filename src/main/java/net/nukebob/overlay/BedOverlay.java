package net.nukebob.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.nukebob.DungeonsBedwars;

import java.util.concurrent.atomic.AtomicBoolean;

public class BedOverlay {
    public static boolean running = false;

    @Environment(EnvType.CLIENT)
    public static void bedDestroyed() {
        final float[] frame = {0};
        final String colour = getTeamColour(MinecraftClient.getInstance());
        AtomicBoolean lRunning = new AtomicBoolean(true);
        if (DeathOverlay.running) {
            HudRenderCallback.EVENT.register((drawContext, tickDeltaManager) -> {
                if (lRunning.get()) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    int screenWidth = client.getWindow().getScaledWidth();
                    int screenHeight = client.getWindow().getScaledHeight();
                    int frameDuration = 3;
                    int fade = 5;
                    Identifier bed;
                    if (Math.floor(frame[0]) <= frameDuration) {
                        bed = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/" + colour + "/0.png");
                    } else if (Math.floor(frame[0]) <= frameDuration * 2) {
                        bed = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/" + colour + "/1.png");
                    } else {
                        bed = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/" + colour + "/2.png");
                    }
                    int bWidth = 16 * 2;
                    int bHeight = 16 * 2;
                    int xShake = 0;
                    int yShake = 0;
                    int xShakeFactor = 5;
                    int yShakeFactor = 5;
                    int fadeOutAfter = 100;
                    if (Math.floor(frame[0]) < 10) {
                        xShake = (int) (xShakeFactor * (1 - (Math.floor(frame[0]) / 10)));
                        if ((int) frame[0] % 2 == 0) xShake *= -1;
                        yShake = (int) (yShakeFactor * (1 - (Math.floor(frame[0]) / 10)));
                        if ((int) frame[0] % 2 == 0) yShake *= -1;
                    }
                    float bxPercent = 0.7f;
                    float byPercent = 0.95f;
                    Identifier text = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/text.png");
                    int tWidth = 327 / 5;
                    int tHeight = 37 / 5;
                    float txPercent = 0.7f;
                    float tyPercent = 0.94f;
                    float shadowOpacity = 0.5f;
                    int shadowOffset = 1;
                    if (Math.floor(frame[0]) < fadeOutAfter) {
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        drawContext.setShaderColor(0.0F, 0.0F, 0.0F, (Math.floor(frame[0]) < fade ? frame[0] / fade * shadowOpacity : shadowOpacity));
                        drawContext.drawTexture(bed, (int) ((screenWidth * bxPercent) - (float) bWidth / 2) + xShake + shadowOffset, (int) ((screenHeight * (1 - byPercent)) + bHeight) + yShake + shadowOffset, 0, 0, bWidth, bHeight, bWidth, bHeight);
                        drawContext.drawTexture(text, (int) ((screenWidth * txPercent) - (float) tWidth / 2) + shadowOffset, (int) ((screenHeight * (1 - tyPercent)) + tHeight) + shadowOffset, 0, 0, tWidth, tHeight, tWidth, tHeight);
                        drawContext.setShaderColor(1.0F, 1.0F, 1.0F, (Math.floor(frame[0]) < fade ? frame[0] / fade : 1));
                        drawContext.drawTexture(bed, (int) ((screenWidth * bxPercent) - (float) bWidth / 2) + xShake, (int) ((screenHeight * (1 - byPercent)) + bHeight) + yShake, 0, 0, bWidth, bHeight, bWidth, bHeight);
                        drawContext.drawTexture(text, (int) ((screenWidth * txPercent) - (float) tWidth / 2), (int) ((screenHeight * (1 - tyPercent)) + tHeight), 0, 0, tWidth, tHeight, tWidth, tHeight);
                        drawContext.drawText(client.textRenderer, "You will no", (int) ((screenWidth * bxPercent)) - 30, (int) ((screenHeight * (1 - 0.75))), Colors.RED, true);
                        drawContext.drawText(client.textRenderer, "longer respawn", (int) ((screenWidth * bxPercent) - 30), (int) ((screenHeight * (1 - 0.74)) + 10), Colors.RED, true);
                        RenderSystem.disableBlend();

                        frame[0] += tickDeltaManager.getLastFrameDuration();
                    } else if (Math.floor(frame[0]) < fadeOutAfter + fade) {
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        drawContext.setShaderColor(0.0F, 0.0F, 0.0F, (1.0f - ((frame[0] + 3 - fadeOutAfter) / (float) fade) * shadowOpacity));
                        drawContext.drawTexture(bed, (int) ((screenWidth * bxPercent) - (float) bWidth / 2) + xShake + shadowOffset, (int) ((screenHeight * (1 - byPercent)) + bHeight) + yShake + shadowOffset, 0, 0, bWidth, bHeight, bWidth, bHeight);
                        drawContext.drawTexture(text, (int) ((screenWidth * txPercent) - (float) tWidth / 2) + shadowOffset, (int) ((screenHeight * (1 - tyPercent)) + tHeight) + shadowOffset, 0, 0, tWidth, tHeight, tWidth, tHeight);
                        drawContext.setShaderColor(1.0F, 1.0F, 1.0F, (1.0f - ((frame[0] - fadeOutAfter) / (float) fade)));
                        drawContext.drawTexture(bed, (int) ((screenWidth * bxPercent) - (float) bWidth / 2) + xShake, (int) ((screenHeight * (1 - byPercent)) + bHeight) + yShake, 0, 0, bWidth, bHeight, bWidth, bHeight);
                        drawContext.drawTexture(text, (int) ((screenWidth * txPercent) - (float) tWidth / 2), (int) ((screenHeight * (1 - tyPercent)) + tHeight), 0, 0, tWidth, tHeight, tWidth, tHeight);
                        drawContext.setShaderColor(1.0F, 1.0F, 1.0F, 1);
                        RenderSystem.disableBlend();

                        frame[0] += tickDeltaManager.getLastFrameDuration();
                    } else {
                        running = false;
                        lRunning.set(false);
                    }
                }
            });
        } else {
            HudRenderCallback.EVENT.register((drawContext, tickDeltaManager) -> {
                int fadeOutAfter = 100;
                if (lRunning.get()) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    int screenWidth = client.getWindow().getScaledWidth();
                    int screenHeight = client.getWindow().getScaledHeight();
                    int frameDuration = 3;
                    int fade = 5;
                    Identifier bed;
                    if (Math.floor(frame[0]) <= frameDuration) {
                        bed = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/" + colour + "/0.png");
                    } else if (Math.floor(frame[0]) <= frameDuration * 2) {
                        bed = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/" + colour + "/1.png");
                    } else {
                        bed = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/" + colour + "/2.png");
                    }
                    int bWidth = 16 * 3;
                    int bHeight = 16 * 3;
                    int xShake = 0;
                    int yShake = 0;
                    int xShakeFactor = 5;
                    int yShakeFactor = 5;

                    if (Math.floor(frame[0]) < 10) {
                        xShake = (int) (xShakeFactor * (1 - (Math.floor(frame[0]) / 10)));
                        if ((int) frame[0] % 2 == 0) xShake *= -1;
                        yShake = (int) (yShakeFactor * (1 - (Math.floor(frame[0]) / 10)));
                        if ((int) frame[0] % 2 == 0) yShake *= -1;
                    }
                    float bxPercent = 0.5f;
                    float byPercent = 0.75f;
                    Identifier text = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/text.png");
                    int tWidth = 327 / 5;
                    int tHeight = 37 / 5;
                    float txPercent = 0.5f;
                    float tyPercent = 0.745f;
                    float shadowOpacity = 0.5f;
                    int shadowOffset = 1;
                    if (Math.floor(frame[0]) < fadeOutAfter) {
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        drawContext.setShaderColor(0.0F, 0.0F, 0.0F, (Math.floor(frame[0]) < fade ? frame[0] / fade * shadowOpacity : shadowOpacity));
                        drawContext.drawTexture(bed, (int) ((screenWidth * bxPercent) - (float) bWidth / 2) + xShake + shadowOffset, (int) ((screenHeight * (1 - byPercent)) + (float) bHeight / 2) + yShake + shadowOffset, 0, 0, bWidth, bHeight, bWidth, bHeight);
                        drawContext.drawTexture(text, (int) ((screenWidth * txPercent) - (float) tWidth / 2) + shadowOffset, (int) ((screenHeight * (1 - tyPercent)) + (float) tHeight / 2) + shadowOffset, 0, 0, tWidth, tHeight, tWidth, tHeight);
                        drawContext.setShaderColor(1.0F, 1.0F, 1.0F, (Math.floor(frame[0]) < fade ? frame[0] / fade : 1));
                        drawContext.drawTexture(bed, (int) ((screenWidth * bxPercent) - (float) bWidth / 2) + xShake, (int) ((screenHeight * (1 - byPercent)) + (float) bHeight / 2) + yShake, 0, 0, bWidth, bHeight, bWidth, bHeight);
                        drawContext.drawTexture(text, (int) ((screenWidth * txPercent) - (float) tWidth / 2), (int) ((screenHeight * (1 - tyPercent)) + (float) tHeight / 2), 0, 0, tWidth, tHeight, tWidth, tHeight);
                        drawContext.drawText(client.textRenderer, "You will no longer respawn", (int) ((screenWidth * bxPercent)) - 65, (int) ((screenHeight * (1 - 0.4))), Colors.RED, true);
                        RenderSystem.disableBlend();

                        frame[0] += tickDeltaManager.getLastFrameDuration();
                    } else if (Math.floor(frame[0]) < fadeOutAfter + fade) {
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        drawContext.setShaderColor(0.0F, 0.0F, 0.0F, (1.0f - ((frame[0] + 3 - fadeOutAfter) / (float) fade) * shadowOpacity));
                        drawContext.drawTexture(bed, (int) ((screenWidth * bxPercent) - (float) bWidth / 2) + xShake + shadowOffset, (int) ((screenHeight * (1 - byPercent)) + (float) bHeight / 2) + yShake + shadowOffset, 0, 0, bWidth, bHeight, bWidth, bHeight);
                        drawContext.drawTexture(text, (int) ((screenWidth * txPercent) - (float) tWidth / 2) + shadowOffset, (int) ((screenHeight * (1 - tyPercent)) + (float) tHeight / 2) + shadowOffset, 0, 0, tWidth, tHeight, tWidth, tHeight);
                        drawContext.setShaderColor(1.0F, 1.0F, 1.0F, (1.0f - ((frame[0] - fadeOutAfter) / (float) fade)));
                        drawContext.drawTexture(bed, (int) ((screenWidth * bxPercent) - (float) bWidth / 2) + xShake, (int) ((screenHeight * (1 - byPercent)) + (float) bHeight / 2) + yShake, 0, 0, bWidth, bHeight, bWidth, bHeight);
                        drawContext.drawTexture(text, (int) ((screenWidth * txPercent) - (float) tWidth / 2), (int) ((screenHeight * (1 - tyPercent)) + (float) tHeight / 2), 0, 0, tWidth, tHeight, tWidth, tHeight);
                        drawContext.setShaderColor(1.0F, 1.0F, 1.0F, 1);
                        RenderSystem.disableBlend();

                        frame[0] += tickDeltaManager.getLastFrameDuration();
                    }
                }
                if (frame[0] > fadeOutAfter) {
                    running = false;
                    lRunning.set(false);
                }
            });
        }
    }

    public static String getTeamColour(MinecraftClient client) {
        ScoreboardObjective sidebar = client.player.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        if (sidebar != null) {
            for (ScoreboardEntry entry : client.player.getScoreboard().getScoreboardEntries(sidebar)) {
                if (entry.display().getString().contains("YOU")) {
                    if (entry.display().getString().contains("Red")) return "red";
                    if (entry.display().getString().contains("Blue")) return "blue";
                    if (entry.display().getString().contains("Green")) return "green";
                    if (entry.display().getString().contains("Yellow")) return "yellow";
                    if (entry.display().getString().contains("Aqua")) return "aqua";
                    if (entry.display().getString().contains("White")) return "white";
                    if (entry.display().getString().contains("Pink")) return "pink";
                    if (entry.display().getString().contains("Gray")) return "gray";
                }
            }
        }
        for (PlayerEntity player : client.player.getWorld().getPlayers()) {
            if (player.getName().getString().equals(client.player.getName().getString())) {
                switch (Integer.toHexString(player.getTeamColorValue())) {
                    case "ff5555": {return "red";}
                    case "5555ff": {return "blue";}
                    case "55ff55": {return "green";}
                    case "ffff55": {return "yellow";}
                    case "55ffff": {return "aqua";}
                    case "ffffff": {return "white";}
                    case "ff55ff": {return "pink";}
                    case "555555": {return "gray";}
                }
            }
        }
        return "white";
    }
}
