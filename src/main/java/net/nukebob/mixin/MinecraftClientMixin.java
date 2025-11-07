package net.nukebob.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.nukebob.DungeonsBedwars;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @WrapOperation(method = "reset", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;stopAll()V"))
    private void dbtitles$restartPersistentSound(SoundManager soundManager, Operation<Void> original) {
        dbtitles$stopAllExceptVictory(soundManager);
    }

    @Unique
    private void dbtitles$stopAllExceptVictory(SoundManager soundManager) {
        for (Identifier key : MinecraftClient.getInstance().getSoundManager().getKeys()) {
            if (key.equals(Identifier.of(DungeonsBedwars.MOD_ID, "finally"))) continue;
            for (SoundCategory value : SoundCategory.values()) {
                soundManager.stopSounds(key, value);
            }
        }
    }
}
