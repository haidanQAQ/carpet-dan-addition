package com.haidan.carpetdan.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.haidan.carpetdan.DeathChestHandler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class DeathChestMixin {
    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"))
    private void dan$captureDeathItems(ServerLevel level, DamageSource source, CallbackInfo ci) {
        if ((Object) this instanceof ServerPlayer player) {
            DeathChestHandler.capture(player, level);
        }
    }

    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    private void dan$placeDeathChest(ServerLevel level, DamageSource source, CallbackInfo ci) {
        if ((Object) this instanceof ServerPlayer player) {
            DeathChestHandler.place(player);
        }
    }
}
