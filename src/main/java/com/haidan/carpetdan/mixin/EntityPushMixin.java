package com.haidan.carpetdan.mixin;

import carpet.patches.EntityPlayerMPFake;
import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * /carpet botnoclip: a Carpet fake player (bot) and a player no longer push
 * each other, so placing a bot and walking into it does not shove it away.
 */
@Mixin(Entity.class)
public abstract class EntityPushMixin {
    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void dan$noBotPlayerPush(Entity other, CallbackInfo ci) {
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
