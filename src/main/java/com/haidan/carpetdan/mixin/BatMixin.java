package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Ported from OptCarpetAddition's removeBats mixin (same inject point and
 * signature): makes the vanilla bat spawn check always fail while the rule is on.
 */
@Mixin(Bat.class)
public class BatMixin {
    @Inject(method = "checkBatSpawnRules", at = @At("HEAD"), cancellable = true)
    private static void dan$cancelBatSpawning(EntityType<Bat> type, LevelAccessor level,
                                              EntitySpawnReason reason, BlockPos pos,
                                              RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetDanSettings.noPhantomBatSpawningEnabled()) {
            cir.setReturnValue(false);
        }
    }
}
