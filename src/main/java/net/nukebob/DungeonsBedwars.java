package net.nukebob;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.nukebob.config.ConfigScreen;
import net.nukebob.overlay.BedOverlay;
import net.nukebob.overlay.DeathOverlay;
import net.nukebob.sound.ModSounds;

@Environment(EnvType.CLIENT)
public class DungeonsBedwars implements ClientModInitializer {
	public static final String MOD_ID = "db";

	@Override
	public void onInitializeClient() {
		ModSounds.registerSounds();

		ConfigScreen configScreen;

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			DeathOverlay.running = false;
			BedOverlay.running = false;
		});
	}
}