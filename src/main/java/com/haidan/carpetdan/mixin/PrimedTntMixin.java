package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Implements {@code TNTdrag}.
 *
 * <p>26.2 changed {@code PrimedTnt.tick()} to scale its velocity by {@code Entity#getAirDrag()},
 * which returns a {@code float} (0.98F). The float is widened back to a {@code double} before
 * {@link net.minecraft.world.phys.Vec3#scale(double)} runs, so the factor is no longer exactly
 * 0.98. This injects only the argument of that single {@code scale(double)} call and, when the
 * rule is {@code doublefloat}, passes the exact double literal 0.98D instead. Gravity, the
 * horizontal 0.7 friction and the -0.5 bounce use {@code multiply(DDD)} and are untouched.</p>
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
}
