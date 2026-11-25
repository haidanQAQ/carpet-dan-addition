package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillager.class)
public class AbstractVillagerMixin {
    @Inject(method = "canBeLeashed", at = @At("HEAD"), cancellable = true)
    private void carpetdan$leashableVillagers(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetDanSettings.leashableVillagersEnabled()) {
            cir.setReturnValue(true);
        }
    }
}
