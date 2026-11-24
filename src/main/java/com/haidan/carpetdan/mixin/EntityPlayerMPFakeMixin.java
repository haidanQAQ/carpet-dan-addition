package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Last line of defence for the {@code botChineseName} rule: Carpet's
 * {@code EntityPlayerMPFake.createFake(...)} returns a boolean, and callers already handle
 * {@code false} by reporting a failure. Rejecting Unicode names here covers every spawn path,
 * including functions, command blocks and nested {@code /execute} commands that never pass
 * through the command-level gate.
 */
@Mixin(targets = "carpet.patches.EntityPlayerMPFake")
public abstract class EntityPlayerMPFakeMixin {
    @Inject(method = "createFake", at = @At("HEAD"), cancellable = true)
    private static void carpetdan$rejectUnicodeNameWhenDisabled(
            String name,
            MinecraftServer server,
            Vec3 pos,
            double yaw,
            double pitch,
            ResourceKey<Level> dimension,
            GameType gameType,
            boolean flying,
            CallbackInfoReturnable<Boolean> cir) {
        if (CarpetDanSettings.botChineseNameEnabled()) {
            return;
        }
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) > 0x7F) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
