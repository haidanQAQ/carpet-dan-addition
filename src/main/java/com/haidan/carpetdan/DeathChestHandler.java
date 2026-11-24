package com.haidan.carpetdan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class DeathChestHandler {
    private static final int SLOTS_PER_CHEST = 27;
    private static final Map<UUID, Pending> PENDING = new HashMap<>();

    private record Pending(ServerLevel level, BlockPos pos, List<ItemStack> items) {
    }

    private DeathChestHandler() {
    }

    public static void capture(ServerPlayer player, ServerLevel level) {
        if (!CarpetDanSettings.deathChestEnabled() || player.isCreative() || player.isSpectator()) {
            return;
        }
        if (level.getGameRules().get(net.minecraft.world.level.gamerules.GameRules.KEEP_INVENTORY)) {
            return;
        }
        List<ItemStack> items = takeDeathItems(player);
        if (!items.isEmpty()) {
            PENDING.put(player.getUUID(), new Pending(level, player.blockPosition(), items));
        }
    }

    public static void place(ServerPlayer player) {
        Pending pending = PENDING.remove(player.getUUID());
        if (pending != null) {
            placeChests(pending.level(), pending.pos(), pending.items());
        }
    }

    private static boolean hasVanishingCurse(ItemStack stack) {
        for (var enchantment : EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet()) {
            if (enchantment.is(Enchantments.VANISHING_CURSE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes everything the player would drop on death from the inventory and returns it.
     * Items with Curse of Vanishing are left alone so vanilla destroys them as usual.
     */
    public static List<ItemStack> takeDeathItems(Player player) {
        Inventory inventory = player.getInventory();
        List<ItemStack> items = new ArrayList<>();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty() || hasVanishingCurse(stack)) {
                continue;
            }
            items.add(stack.copy());
        }
        if (items.isEmpty()) {
            return items;
        }
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            inventory.setItem(slot, ItemStack.EMPTY);
        }
        return items;
    }

    public static void placeChests(ServerLevel level, BlockPos deathPos, List<ItemStack> items) {
        if (items.isEmpty()) {
            return;
        }
        int chestCount = (items.size() + SLOTS_PER_CHEST - 1) / SLOTS_PER_CHEST;
        BlockPos spot = findSpot(level, deathPos, chestCount);
        if (spot == null) {
            dropRemaining(level, deathPos, items, 0);
            return;
        }

        int index = 0;
        for (int chest = 0; chest < chestCount; chest++) {
            BlockPos pos = spot.above(chest);
            level.setBlock(pos, Blocks.CHEST.defaultBlockState(), 3);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof ChestBlockEntity chestEntity)) {
                dropRemaining(level, deathPos, items, index);
                return;
            }
            for (int slot = 0; slot < SLOTS_PER_CHEST && index < items.size(); slot++, index++) {
                chestEntity.setItem(slot, items.get(index));
            }
            chestEntity.setChanged();
        }
    }

    private static BlockPos findSpot(ServerLevel level, BlockPos deathPos, int chestCount) {
        int minY = level.getMinY();
        int maxY = level.getMaxY();
        int topY = maxY - chestCount + 1;
        if (topY < minY) {
            return null;
        }

        int x = deathPos.getX();
        int y0 = deathPos.getY();
        int z = deathPos.getZ();

        if (y0 > maxY) {
            // above the world: start from the top and go down
            for (int y = topY; y >= minY; y--) {
                if (isFree(level, x, y, z, chestCount)) {
                    return new BlockPos(x, y, z);
                }
            }
            return null;
        }
        if (y0 < minY) {
            // below the world: start from the bottom and go up
            for (int y = minY; y <= topY; y++) {
                if (isFree(level, x, y, z, chestCount)) {
                    return new BlockPos(x, y, z);
                }
            }
            return null;
        }

        // inside the world: search upwards first, then downwards
        for (int y = y0 + 1; y <= topY; y++) {
            if (isFree(level, x, y, z, chestCount)) {
                return new BlockPos(x, y, z);
            }
        }
        for (int y = y0 - 1; y >= minY; y--) {
            if (isFree(level, x, y, z, chestCount)) {
                return new BlockPos(x, y, z);
            }
        }

        // last resort: overwrite blocks in place, but never a block entity; keep going up
        for (int y = y0; y <= topY; y++) {
            if (!hasBlockEntity(level, x, y, z, chestCount)) {
                return new BlockPos(x, y, z);
            }
        }
        return null;
    }

    private static boolean isFree(ServerLevel level, int x, int y, int z, int chestCount) {
        for (int offset = 0; offset < chestCount; offset++) {
            BlockState state = level.getBlockState(new BlockPos(x, y + offset, z));
            if (!state.isAir()) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasBlockEntity(ServerLevel level, int x, int y, int z, int chestCount) {
        for (int offset = 0; offset < chestCount; offset++) {
            if (level.getBlockEntity(new BlockPos(x, y + offset, z)) != null) {
                return true;
            }
        }
        return false;
    }

    private static void dropRemaining(ServerLevel level, BlockPos pos, List<ItemStack> items, int from) {
        for (int i = from; i < items.size(); i++) {
            Block.popResource(level, pos, items.get(i));
        }
    }
}
