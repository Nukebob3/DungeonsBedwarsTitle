package net.nukebob.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.text.Text;
import net.nukebob.config.ConfigManager;
import net.nukebob.overlay.BedOverlay;
import net.nukebob.overlay.DeathOverlay;
import net.nukebob.overlay.VictoryOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayerEntityMixin {

	@Unique
	private static String title = "";
	@Unique
	private static String subtitle = "";

	@Inject(method = "onTitle", at = @At("TAIL"))
	private void setTitle(TitleS2CPacket packet, CallbackInfo ci) {
		if (ConfigManager.loadConfig().mod_enabled) {
			title = packet.text().getString().toLowerCase();
			if (packet.text().getString().toLowerCase().contains("victory") && ConfigManager.loadConfig().victory_enabled) {
				MinecraftClient client = MinecraftClient.getInstance();
				client.inGameHud.setSubtitle(Text.literal(""));
				client.inGameHud.setTitle(Text.literal(""));
				if (!VictoryOverlay.running) {
					VictoryOverlay.running = true;
					VictoryOverlay.displayImage(client.player);
				}
			}
			else if (packet.text().getString().toLowerCase().contains("died") && ConfigManager.loadConfig().death_enabled) {
				if (subtitle.contains("5")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					if (!DeathOverlay.running) {
						DeathOverlay.running = true;
						DeathOverlay.displayImage(client.player);
					}
				} else if (subtitle.contains("4")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					DeathOverlay.countdown = 3;
				} else if (subtitle.contains("3")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					DeathOverlay.countdown = 2;
				} else if (subtitle.contains("2")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					DeathOverlay.countdown = 1;
				} else if (subtitle.contains("1")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					DeathOverlay.countdown = 0;
				}
			}
			else if (packet.text().getString().toLowerCase().contains("bed destroyed") && ConfigManager.loadConfig().bed_enabled) {
				MinecraftClient client = MinecraftClient.getInstance();
				client.inGameHud.setSubtitle(Text.literal(""));
				client.inGameHud.setTitle(Text.literal(""));
				if (!BedOverlay.running) {
					BedOverlay.running = true;
					BedOverlay.bedDestroyed();
				}
			}
			subtitle = "";
		}
		if (packet.text().getString().toLowerCase().contains("respawned")) {
			DeathOverlay.running = false;
		}
	}
	@Inject(method = "onSubtitle", at = @At("TAIL"))
	private void setSubtitle(SubtitleS2CPacket packet, CallbackInfo ci) {
		if (ConfigManager.loadConfig().mod_enabled) {
			subtitle = packet.text().getString().toLowerCase();
			if (title.contains("died") && ConfigManager.loadConfig().death_enabled) {
				if (subtitle.contains("5")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					if (!DeathOverlay.running) {
						DeathOverlay.running = true;
						DeathOverlay.displayImage(client.player);
					}
				} else if (subtitle.contains("4")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					DeathOverlay.countdown = 3;
				} else if (subtitle.contains("3")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					DeathOverlay.countdown = 2;
				} else if (subtitle.contains("2")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					DeathOverlay.countdown = 1;
				} else if (subtitle.contains("1")) {
					MinecraftClient client = MinecraftClient.getInstance();
					client.inGameHud.setSubtitle(Text.literal(""));
					client.inGameHud.setTitle(Text.literal(""));
					DeathOverlay.countdown = 0;
				}
			}
			else if (packet.text().getString().toLowerCase().contains("no longer respawn") && ConfigManager.loadConfig().bed_enabled) {
				MinecraftClient client = MinecraftClient.getInstance();
				client.inGameHud.setSubtitle(Text.literal(""));
				client.inGameHud.setTitle(Text.literal(""));
			}
			title = "";
		}
	}
}