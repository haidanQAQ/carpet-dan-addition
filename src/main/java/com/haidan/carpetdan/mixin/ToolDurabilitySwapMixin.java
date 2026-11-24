package com.haidan.carpetdan.mixin;

import carpet.patches.EntityPlayerMPFake;
import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Part of {@code botToolSwap}: when a bot's held tool is nearly broken, swap it for the same tool
 * with the most remaining durability in the inventory. Only runs when durability is actually lost,
 * and only for the main-hand tool of a Carpet fake player.
 */
@Mixin(ItemStack.class)
public abstract class ToolDurabilitySwapMixin {
    /** The tool item that is about to take damage; kept across the HEAD/TAIL pair of one call. */
    @Unique
    private static final ThreadLocal<Item> carpetdan$toolBeingDamaged = new ThreadLocal<>();

    @Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V", at = @At("HEAD"))
    private void carpetdan$rememberDamagedTool(int amount, ServerLevel level, ServerPlayer player,
                                               Consumer<ItemStack> onBroken, CallbackInfo ci) {
        carpetdan$toolBeingDamaged.remove();
        ItemStack self = (ItemStack) (Object) this;
        if (player instanceof EntityPlayerMPFake
                && CarpetDanSettings.botToolSwapEnabled()
                && self.isDamageableItem()
                && !self.has(DataComponents.EQUIPPABLE)
                && player.getInventory().getSelectedItem() == self) {
            carpetdan$toolBeingDamaged.set(self.getItem());
        }
    }

    @Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private void carpetdan$swapWhenNearlyBroken(int amount, ServerLevel level, ServerPlayer player,
                                                Consumer<ItemStack> onBroken, CallbackInfo ci) {
        Item tool = carpetdan$toolBeingDamaged.get();
        carpetdan$toolBeingDamaged.remove();
        if (tool == null || !(player instanceof EntityPlayerMPFake)) {
            return;
        }

        ItemStack self = (ItemStack) (Object) this;
        Inventory inventory = player.getInventory();
        boolean broken = self.isEmpty();
        if (!broken && inventory.getSelectedItem() != self) {
            return; // the bot already switched to something else
        }
        if (broken && !inventory.getSelectedItem().isEmpty()) {
            return;
        }

        int maxDamage = self.getMaxDamage();
        int remaining = maxDamage - self.getDamageValue();
        if (!broken) {
            int threshold = Math.max(1, maxDamage / 10);
            if (remaining > threshold) {
                return;
            }
        }

        int bestSlot = -1;
        int bestRemaining = -1;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty() || stack == self || !stack.is(tool) || !stack.isDamageableItem()) {
                continue;
            }
            int stackRemaining = stack.getMaxDamage() - stack.getDamageValue();
            if (stackRemaining > bestRemaining) {
                bestRemaining = stackRemaining;
                bestSlot = slot;
            }
        }
        if (bestSlot < 0) {
            return;
        }
        if (!broken) {
            int margin = Math.max(1, maxDamage / 10);
            if (bestRemaining <= remaining + margin) {
                return; // not worth the swap yet
            }
        }

        ItemStack candidate = inventory.getItem(bestSlot);
        if (broken) {
            inventory.setItem(bestSlot, ItemStack.EMPTY);
            inventory.setSelectedItem(candidate);
        } else {
            inventory.setItem(bestSlot, self.copy());
            inventory.setSelectedItem(candidate);
        }
    }
}
