package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Implements {@code villagerneverstoptrading}.
 *
 * <p>Vanilla locks a trade once {@code uses >= maxUses} and raises the price through
 * {@code demand}, which is only updated when a villager restocks. While the rule is on the
 * uses counter is frozen: {@code isOutOfStock()} stays false, {@code needsRestock()} stays
 * false, so a trade never disappears and the restock loop never raises demand either. That
 * makes the rule O(1) per trade: three cheap checks, no NBT rewriting and no command
 * execution (unlike the advancement/datapack approach, which runs a function and rewrites
 * up to 50 offers for every villager near the player on every trade).</p>
 */
@Mixin(MerchantOffer.class)
public abstract class MerchantOfferMixin {

    @Inject(method = "increaseUses", at = @At("HEAD"), cancellable = true)
    private void dan$freezeUses(CallbackInfo ci) {
        if (CarpetDanSettings.villagerNeverStopTrading()) {
            ci.cancel();
        }
    }

    @Inject(method = "isOutOfStock", at = @At("HEAD"), cancellable = true)
    private void dan$neverOutOfStock(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetDanSettings.villagerNeverStopTrading()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "needsRestock", at = @At("HEAD"), cancellable = true)
    private void dan$neverNeedsRestock(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetDanSettings.villagerNeverStopTrading()) {
            cir.setReturnValue(false);
        }
    }
}
