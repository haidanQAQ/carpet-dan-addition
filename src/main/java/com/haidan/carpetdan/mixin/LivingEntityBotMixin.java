package com.haidan.carpetdan.mixin;

import carpet.patches.EntityPlayerMPFake;
import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * /carpet botnoclip: bots and players may overlap without pushing each other.
 *
 * <p>Two hooks are needed because entity pushing happens through
 * {@code LivingEntity.pushEntities() -> doPush(Entity) -> Entity.push(Entity)}:
 * a fake player is taken out of the "pushable entities" list, and the
 * doPush step is cancelled for bot/player pairs. The second one also covers
 * the direction where the bot is the one running pushEntities().</p>
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityBotMixin {
    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private void dan$fakeNotPushable(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetDanSettings.botNoClip() && (Object) this instanceof EntityPlayerMPFake) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "doPush", at = @At("HEAD"), cancellable = true)
    private void dan$noBotPlayerDoPush(Entity other, CallbackInfo ci) {
        if (!CarpetDanSettings.botNoClip()) {
            return;
        }
        Entity self = (Entity) (Object) this;
        if (!(self instanceof Player) || !(other instanceof Player)) {
            return;
        }
        if (self instanceof EntityPlayerMPFake || other instanceof EntityPlayerMPFake) {
            ci.cancel();
        }
    }
}
