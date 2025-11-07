package net.nukebob.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.text.Text;
import net.nukebob.config.dbtitles.DungeonsBedwarsTitlesConfig;
import net.nukebob.overlay.BedOverlay;
import net.nukebob.overlay.DeathOverlay;
import net.nukebob.overlay.VictoryOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetAddress;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayerEntityMixin {

	@Unique
	private static String title = "";
	@Unique
	private static String subtitle = "";

	@Inject(method = "onTitle", at = @At("TAIL"))
	private void dbtitles$setTitle(TitleS2CPacket packet, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		DungeonsBedwarsTitlesConfig config = DungeonsBedwarsTitlesConfig.loadConfig();
		if (config.mod_enabled&&(dbtitles$isOnHypixel()|| !config.hypixel_only)) {
			title = packet.text().getString().toLowerCase();
			if (packet.text().getString().toLowerCase().contains("victory") && config.victory_enabled) {
				dbtitles$clearTitles(client);
				if (!VictoryOverlay.running) {
					VictoryOverlay.display();
				}
			}
			else if (packet.text().getString().toLowerCase().contains("died") && config.death_enabled) {
				if (subtitle.contains("5")) {
					dbtitles$clearTitles(client);
					if (!DeathOverlay.running) {
						DeathOverlay.display();
					}
				} else if (subtitle.contains("4")) {
					dbtitles$clearTitles(client);
					DeathOverlay.countdown = 3;
				} else if (subtitle.contains("3")) {
					dbtitles$clearTitles(client);
					DeathOverlay.countdown = 2;
				} else if (subtitle.contains("2")) {
					dbtitles$clearTitles(client);
					DeathOverlay.countdown = 1;
				} else if (subtitle.contains("1")) {
					dbtitles$clearTitles(client);
					DeathOverlay.countdown = 0;
				}
			}
			else if (packet.text().getString().toLowerCase().contains("bed destroyed") && config.bed_enabled) {
				dbtitles$clearTitles(client);
				if (!BedOverlay.running) {
					BedOverlay.display();
				}
			}
			subtitle = "";
		}
		//Clear even outside Hypixel
		if (packet.text().getString().toLowerCase().contains("respawned")) {
			DeathOverlay.running = false;
		}
	}
	@Inject(method = "onSubtitle", at = @At("TAIL"))
	private void dbtitles$setSubtitle(SubtitleS2CPacket packet, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		DungeonsBedwarsTitlesConfig config = DungeonsBedwarsTitlesConfig.loadConfig();
		if (config.mod_enabled&&(dbtitles$isOnHypixel()|| !config.hypixel_only)) {
			subtitle = packet.text().getString().toLowerCase();
			if (title.contains("died") && config.death_enabled) {
				if (subtitle.contains("5")) {
					dbtitles$clearTitles(client);
					if (!DeathOverlay.running) {
						DeathOverlay.display();
					}
				} else if (subtitle.contains("4")) {
					dbtitles$clearTitles(client);
					DeathOverlay.countdown = 3;
				} else if (subtitle.contains("3")) {
					dbtitles$clearTitles(client);
					DeathOverlay.countdown = 2;
				} else if (subtitle.contains("2")) {
					dbtitles$clearTitles(client);
					DeathOverlay.countdown = 1;
				} else if (subtitle.contains("1")) {
					dbtitles$clearTitles(client);
					DeathOverlay.countdown = 0;
				}
			}
		}
	}

	@Unique
	private static void dbtitles$clearTitles(MinecraftClient client) {
		client.inGameHud.setSubtitle(Text.literal(""));
		client.inGameHud.setTitle(Text.literal(""));
	}

	@Unique
	private static boolean dbtitles$isOnHypixel() {
		MinecraftClient client = MinecraftClient.getInstance();
		ServerInfo server = client.getCurrentServerEntry();
		if (server == null) return false;

		try {
			InetAddress inet = InetAddress.getByName(server.address);
			String host = inet.getHostName().toLowerCase();

			return host.endsWith("hypixel.net");
		} catch (Exception e) {
			return false;
		}
	}
}