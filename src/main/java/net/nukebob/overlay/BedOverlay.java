package net.nukebob.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import net.nukebob.DungeonsBedwars;

import java.awt.*;

public class BedOverlay implements HudLayerRegistrationCallback {
    public static float frame = 0;
    public static boolean running = false;
    private static String colour = "white";

    public static void display() {
        frame = 0;
        running = true;
        colour = getTeamColour();
    }

    @Override
    public void register(LayeredDrawerWrapper layeredDrawerWrapper) {
        layeredDrawerWrapper.addLayer(new IdentifiedLayer() {
            @Override
            public Identifier id() {
                return Identifier.of(DungeonsBedwars.MOD_ID, "bed_overlay");
            }

            @Override
            public void render(DrawContext context, RenderTickCounter tickCounter) {
                if (!running) return;

                MinecraftClient client = MinecraftClient.getInstance();
                boolean deathRunning = DeathOverlay.running;

                // ─── Configuration ─────────────────────────────
                int fadeOutAfter = 100, fade = 5;
                int screenWidth = context.getScaledWindowWidth();
                int screenHeight = context.getScaledWindowHeight();
                int frameDuration = 3;

                // ─── Frame animation ───────────────────────────
                int frameIndex = Math.min(2, (int) (frame / frameDuration));
                Identifier bed = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/" + colour + "/" + frameIndex);
                Identifier text = Identifier.of(DungeonsBedwars.MOD_ID, "overlay/bed/text");

                // ─── Shake calculation ─────────────────────────
                int xShake = 0, yShake = 0;
                int shakeFactor = 5;
                if (frame < 10) {
                    xShake = (int) (shakeFactor * (1 - frame / 10f)) * ((int) frame % 2 == 0 ? -1 : 1);
                    yShake = (int) (shakeFactor * (1 - frame / 10f)) * ((int) frame % 2 == 0 ? -1 : 1);
                }

                // ─── Positioning ──────────────────────────────
                float bxPercent = deathRunning ? 0.7f : 0.5f;
                float byPercent = deathRunning ? 0.95f : 0.75f;
                float txPercent = deathRunning ? 0.7f : 0.5f;
                float tyPercent = deathRunning ? 0.94f : 0.745f;

                int bWidth = deathRunning ? 32 : 48; // 16*2 or 16*3
                int bHeight = deathRunning ? 32 : 48;
                int tWidth = 327 / 5;
                int tHeight = 37 / 5;
                int shadowOffset = 1;
                float shadowOpacity = 0.5f;

                // ─── Fade calculation ─────────────────────────
                float fadeProgress = frame < fade ? frame / (float) fade : 1f;
                float fadeOutProgress = Math.max(0f, 1f - (frame - fadeOutAfter) / (float) fade);
                float shadowAlpha = frame < fadeOutAfter ? fadeProgress * shadowOpacity : fadeOutProgress * shadowOpacity;
                Color shadowColor = new Color(0f, 0f, 0f, Math.clamp(shadowAlpha, 0, 1));
                Color mainColor = new Color(1f, 1f, 1f, Math.clamp(frame < fadeOutAfter ? fadeProgress : fadeOutProgress, 0, 1));
                Color redColor = new Color(1f, 0f, 0f, mainColor.getAlpha() / 255f);

                // ─── Draw textures ────────────────────────────
                int bedX = (int) ((screenWidth * bxPercent) - (float) bWidth / 2) + xShake;
                int bedY = (int) ((screenHeight * (1 - byPercent)) + (deathRunning ? bHeight : (float) bHeight / 2)) + yShake;
                int textX = (int) ((screenWidth * txPercent) - (float) tWidth / 2);
                int textY = (int) ((screenHeight * (1 - tyPercent)) + (deathRunning ? tHeight : (float) tHeight / 2));

                // Shadow
                context.drawGuiTexture(RenderLayer::getGuiTextured, bed, bedX + shadowOffset, bedY + shadowOffset, bWidth, bHeight, shadowColor.getRGB());
                context.drawGuiTexture(RenderLayer::getGuiTextured, text, textX + shadowOffset, textY + shadowOffset, tWidth, tHeight, shadowColor.getRGB());

                // Main
                context.drawGuiTexture(RenderLayer::getGuiTextured, bed, bedX, bedY, bWidth, bHeight, mainColor.getRGB());
                context.drawGuiTexture(RenderLayer::getGuiTextured, text, textX, textY, tWidth, tHeight, mainColor.getRGB());

                // ─── Draw text ────────────────────────────────
                if (deathRunning) {
                    context.drawText(client.textRenderer, "You will no", bedX - 2, (int) (screenHeight * 0.25), redColor.getRGB(), true);
                    context.drawText(client.textRenderer, "longer respawn", bedX - 2, (int) (screenHeight * 0.25) + 10, redColor.getRGB(), true);
                } else {
                    context.drawText(client.textRenderer, "You will no longer respawn", bedX - 30, (int) (screenHeight * 0.6), redColor.getRGB(), true);
                }

                // ─── Update frame ─────────────────────────────
                if (!client.isPaused()) frame += tickCounter.getDynamicDeltaTicks();
                if (frame > fadeOutAfter + fade) running = false;
            }
        });
    }

    public static String getTeamColour() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player==null) return "white";

        ScoreboardObjective sidebar = client.player.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        if (sidebar != null) {
            for (ScoreboardEntry entry : client.player.getScoreboard().getScoreboardEntries(sidebar)) {
                if (entry.display() != null) {
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
