package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.advancements.predicates.entity.FishingHookPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Pre-1.16 fishing: make the in_open_water loot condition always pass.
 * The treasure entry still has its normal 5% weight, so this reproduces the old feel.
 */
@Mixin(FishingHookPredicate.class)
public abstract class FishingHookPredicateMixin {
    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private void dan$ignoreOpenWater(Entity entity, ServerLevel level, Vec3 pos,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (CarpetDanSettings.ignoreOpenWater()) {
            cir.setReturnValue(true);
        }
    }
}
