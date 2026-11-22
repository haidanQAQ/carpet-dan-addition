package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets a hoe break bedrock when {@code hoebedrockbreaking} is on.
 *
 * <p>The progress is computed as if a pickaxe were mining stone: the hoe's tier speed,
 * Efficiency, Haste, Mining Fatigue and the block-break-speed attribute are taken from
 * {@link Player#getDestroySpeed} on a hoe-mineable block, then divided by stone's hardness
 * (1.5) and the 30x correct-tool divisor. Diamond+ with Efficiency V and Haste II breaks
 * in a single game tick.</p>
 */
@Mixin(BlockBehaviour.class)
public abstract class BlockMixin {
    @Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
    private void carpetdan$hoeBedrockProgress(BlockState state, Player player, BlockGetter level, BlockPos pos,
                                              CallbackInfoReturnable<Float> cir) {
        if (!CarpetDanSettings.hoeBedrockBreaking() || !state.is(Blocks.BEDROCK)) {
            return;
        }
        if (!player.getMainHandItem().is(ItemTags.HOES)) {
            return;
        }
        float speed = player.getDestroySpeed(Blocks.HAY_BLOCK.defaultBlockState());
        cir.setReturnValue(speed / 1.5F / 30.0F);
    }
}
