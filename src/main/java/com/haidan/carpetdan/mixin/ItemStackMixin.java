package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Consumes exactly one point of hoe durability when a bedrock block is broken with it.
 * Unbreaking is handled by {@link ItemStack#hurtAndBreak(int, net.minecraft.world.entity.LivingEntity, EquipmentSlot)}.
 * Vanilla never damages the hoe for bedrock, so the vanilla call is cancelled to avoid double damage.
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "mineBlock", at = @At("HEAD"), cancellable = true)
    private void carpetdan$hoeBedrockDurability(Level level, BlockState state, BlockPos pos, Player player,
                                                CallbackInfo ci) {
        if (!CarpetDanSettings.hoeBedrockBreaking() || !state.is(Blocks.BEDROCK)) {
            return;
        }
        ItemStack self = (ItemStack) (Object) this;
        if (!self.is(ItemTags.HOES)) {
            return;
        }
        if (!player.isCreative()) {
            self.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
        ci.cancel();
    }
}
