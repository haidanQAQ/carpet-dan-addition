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
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
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

        if (!(player instanceof EntityPlayerMPFake)) {
            return;
        }
        if (!CarpetDanSettings.botToolSwapEnabled()) {
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
        boolean sameKind = sameToolType(current, before);
        boolean better = isBetter(current, before);
        // Empty main hand: no comparison, just let the item stay in the inventory.
        // Only compare tools of the same kind (a bow must never replace a fishing rod),
        // while a stone pickaxe may still be replaced by a better pickaxe.
        if (current.isEmpty() || !isTool(current) || !sameKind || !better) {
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

    private static boolean sameToolType(ItemStack a, ItemStack b) {
        if (ItemStack.isSameItem(a, b)) {
            return true;
        }
        return (a.is(ItemTags.PICKAXES) && b.is(ItemTags.PICKAXES))
                || (a.is(ItemTags.AXES) && b.is(ItemTags.AXES))
                || (a.is(ItemTags.SHOVELS) && b.is(ItemTags.SHOVELS))
                || (a.is(ItemTags.HOES) && b.is(ItemTags.HOES))
                || (a.is(ItemTags.SWORDS) && b.is(ItemTags.SWORDS))
                || (a.getItem() instanceof ShearsItem && b.getItem() instanceof ShearsItem);
    }

    private static boolean isBetter(ItemStack current, ItemStack candidate) {
        boolean currentMending = hasMending(current);
        boolean candidateMending = hasMending(candidate);
        if (currentMending != candidateMending) {
            return candidateMending;
        }
        int currentScore = score(current);
        int candidateScore = score(candidate);
        if (candidateScore != currentScore) {
            return candidateScore > currentScore;
        }
        // Same enchantment total (often both plain): fall back to the material tier.
        return candidate.getMaxDamage() > current.getMaxDamage();
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
