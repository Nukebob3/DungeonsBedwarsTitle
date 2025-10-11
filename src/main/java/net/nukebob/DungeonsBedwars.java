package net.nukebob;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.nukebob.overlay.BedOverlay;
import net.nukebob.overlay.DeathOverlay;
import net.nukebob.overlay.VictoryOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class DungeonsBedwars implements ClientModInitializer {
	public static final String MOD_ID = "db";
	public static final Logger LOGGER = LoggerFactory.getLogger(DungeonsBedwars.class);

	@Override
	public void onInitializeClient() {
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			DeathOverlay.running = false;
			BedOverlay.running = false;
		});

		VictoryOverlay.EVENT.register(new VictoryOverlay());
		DeathOverlay.EVENT.register(new DeathOverlay());
		BedOverlay.EVENT.register(new BedOverlay());

		LOGGER.info("Loaded Dungeons Bedwars by Nukebob3!");
	}
}