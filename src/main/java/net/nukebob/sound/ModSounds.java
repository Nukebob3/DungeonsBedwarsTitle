package net.nukebob.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.nukebob.DungeonsBedwars;

@Environment(EnvType.CLIENT)
public class ModSounds {
    public static final SoundEvent VICTORY = registerSoundEvent("finally");
    public static final SoundEvent DEATH = registerSoundEvent("death");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(DungeonsBedwars.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        System.out.println("Registering sounds");
    }
}
