package net.nukebob;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nukebob.overlay.BedOverlay;
import net.nukebob.overlay.DeathOverlay;
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

		try {
			ResourceManagerHelperImpl.registerBuiltinResourcePack(Identifier.of(MOD_ID, "dungeons_bedwars"), "resourcepacks/" + Identifier.of(MOD_ID, "dungeons_bedwars").getPath(), FabricLoader.getInstance().getModContainer(MOD_ID).get(), Text.literal("Dungeons Bedwars"), ResourcePackActivationType.NORMAL);
		} catch (Exception e) {
			LOGGER.error("Failed to register builtin resourcepack", e);
		}
	}
}