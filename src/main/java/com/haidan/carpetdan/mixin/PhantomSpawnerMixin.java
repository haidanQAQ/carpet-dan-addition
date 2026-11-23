package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stops the vanilla phantom spawner from running while the rule is enabled.
 * Phantoms only enter the world through {@link PhantomSpawner#tick}, so cancelling
 * the tick disables natural phantom spawning completely (commands and spawn eggs
 * still work).
 */
@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void dan$cancelPhantomSpawning(ServerLevel level, boolean spawnEnemies, CallbackInfo ci) {
        if (CarpetDanSettings.noPhantomBatSpawningEnabled()) {
            ci.cancel();
        }
    }
}
