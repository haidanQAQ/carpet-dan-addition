package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import com.haidan.carpetdan.ticket.TntChunkLoadingTicket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Implements {@code TNTdrag} and {@code TNTchunkloading}.
 *
 * <p>{@code TNTdrag}: 26.2 changed {@code PrimedTnt.tick()} to scale its velocity by
 * {@code Entity#getAirDrag()}, which returns a {@code float} (0.98F). The float is widened back to a
 * {@code double} before {@link Vec3#scale(double)} runs, so the factor is no longer exactly 0.98.
 * The {@link ModifyArg} below injects only the argument of that single {@code scale(double)} call
 * and, when the rule is {@code doublefloat}, passes the exact double literal 0.98D instead. Gravity,
 * the 0.7 horizontal friction and the -0.5 bounce use {@code multiply(DDD)} and are untouched.</p>
 *
 * <p>{@code TNTchunkloading}: when enabled, a primed TNT that moves horizontally faster than the
 * configured threshold places an 80 gt loading ticket (same flags and radius as an ender pearl
 * ticket) on the chunk it currently occupies.</p>
 */
@Mixin(PrimedTnt.class)
public abstract class PrimedTntMixin {

    @ModifyArg(
            method = "tick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"),
            index = 0)
    private double carpetdan$tntAirDrag(double drag) {
        return CarpetDanSettings.tntDoubleDrag() ? 0.98D : drag;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void carpetdan$tntChunkLoading(CallbackInfo ci) {
        double threshold = CarpetDanSettings.tntChunkLoadingThreshold();
        if (threshold < 0.0D) {
            return;
        }
        PrimedTnt self = (PrimedTnt) (Object) this;
        if (self.isRemoved() || !(self.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        Vec3 motion = self.getDeltaMovement();
        if (Math.abs(motion.x) > threshold || Math.abs(motion.z) > threshold) {
            // Same radius (2) that ServerPlayer.placeEnderPearlTicket uses for ENDER_PEARL.
            serverLevel.getChunkSource().addTicketWithRadius(
                    TntChunkLoadingTicket.TNT_CHUNK_LOADING,
                    self.chunkPosition(),
                    2);
        }
    }
}
