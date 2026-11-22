package com.haidan.carpetdan.mixin;

import carpet.patches.EntityPlayerMPFake;
import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * /carpet botToolSwap: when a Carpet fake player picks up a tool, compare it with
 * the tool in its main hand and equip the better one.
 *
 * <p>Mending wins first; otherwise the higher total enchantment level wins, with
 * every curse level counted as -10.</p>
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityToolSwapMixin {
    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void dan$botToolSwap(Player player, CallbackInfo ci) {
        if (!CarpetDanSettings.botToolSwapEnabled()) {
            return;
        }
        if (!(player instanceof EntityPlayerMPFake)) {
            return;
        }

        ItemEntity self = (ItemEntity) (Object) this;
        ItemStack incoming = self.getItem();
        if (incoming.isEmpty() || incoming.getCount() <= 0 || !isTool(incoming)) {
            return;
        }

        ItemStack current = player.getMainHandItem();
        if (!current.isEmpty() && (!isTool(current) || sameItem(current, incoming)
                || !isBetter(current, incoming))) {
            return;
        }

        ItemStack picked = incoming.copyWithCount(1);
        incoming.shrink(1);
        if (incoming.isEmpty()) {
            self.discard();
        }

        player.setItemInHand(InteractionHand.MAIN_HAND, picked);
        if (!current.isEmpty()) {
            if (!player.getInventory().add(current.copy())) {
                player.spawnAtLocation((net.minecraft.server.level.ServerLevel) player.level(), current.copy());
            }
        }
    }

    private static boolean isTool(ItemStack stack) {
        return stack.isDamageableItem() && !stack.has(DataComponents.EQUIPPABLE);
    }

    private static boolean sameItem(ItemStack a, ItemStack b) {
        return ItemStack.isSameItemSameComponents(a, b);
    }

    private static boolean isBetter(ItemStack current, ItemStack candidate) {
        boolean currentMending = hasMending(current);
        boolean candidateMending = hasMending(candidate);
        if (currentMending != candidateMending) {
            return candidateMending;
        }
        return score(candidate) > score(current);
    }

    private static boolean hasMending(ItemStack stack) {
        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        for (Holder<Enchantment> enchantment : enchantments.keySet()) {
            if (enchantment.is(Enchantments.MENDING)) {
                return enchantments.getLevel(enchantment) > 0;
            }
        }
        return false;
    }

    private static int score(ItemStack stack) {
        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        int total = 0;
        for (var entry : enchantments.entrySet()) {
            int level = entry.getIntValue();
            if (entry.getKey().is(EnchantmentTags.CURSE)) {
                total -= 10 * level;
            } else {
                total += level;
            }
        }
        return total;
    }
}
