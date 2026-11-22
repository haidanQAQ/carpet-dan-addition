package com.haidan.carpetdan.mixin;

import carpet.patches.EntityPlayerMPFake;
import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
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
 * the tool already in its main hand and equip the better one.
 *
 * <p>Vanilla pickup rules (pickup delay, target UUID, inventory space) run first.
 * We only act after the item has actually entered the bot's inventory, so dropped
 * items are not vacuumed off the ground. An empty main hand is left untouched.</p>
 *
 * <p>Mending wins first; otherwise the higher total enchantment level wins, with
 * every curse level counted as -10.</p>
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityToolSwapMixin {
    private static final ThreadLocal<ItemStack> DAN$PENDING_PICKUP =
            ThreadLocal.withInitial(() -> ItemStack.EMPTY);

    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void dan$capturePickup(Player player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        DAN$PENDING_PICKUP.set(self.getItem().copy());
    }

    @Inject(method = "playerTouch", at = @At("TAIL"))
    private void dan$botToolSwap(Player player, CallbackInfo ci) {
        ItemStack before = DAN$PENDING_PICKUP.get();
        DAN$PENDING_PICKUP.set(ItemStack.EMPTY);

        if (!CarpetDanSettings.botToolSwapEnabled()) {
            return;
        }
        if (!(player instanceof EntityPlayerMPFake)) {
            return;
        }
        if (before.isEmpty() || !isTool(before)) {
            return;
        }

        ItemEntity self = (ItemEntity) (Object) this;
        boolean didPickup = self.isRemoved() || self.getItem().getCount() < before.getCount();
        if (!didPickup) {
            return;
        }

        ItemStack current = player.getMainHandItem();
        // Empty main hand: no comparison, just let the item stay in the inventory.
        if (current.isEmpty() || !isTool(current) || sameItem(current, before)
                || !isBetter(current, before)) {
            return;
        }

        ItemStack picked = before.copyWithCount(1);
        int invSlot = -1;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (ItemStack.isSameItemSameComponents(player.getInventory().getItem(i), picked)) {
                invSlot = i;
                break;
            }
        }
        if (invSlot >= 0) {
            player.getInventory().removeItem(invSlot, 1);
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, picked);
        if (!player.getInventory().add(current.copy())) {
            player.spawnAtLocation((ServerLevel) player.level(), current.copy());
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
