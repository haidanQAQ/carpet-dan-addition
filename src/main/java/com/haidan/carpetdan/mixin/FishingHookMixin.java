package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * IMBA mode: retrieve() always uses the treasure loot table.
 */
@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Redirect(
            method = "retrieve",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETSTATIC,
                    target = "Lnet/minecraft/world/level/storage/loot/BuiltInLootTables;FISHING:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private static ResourceKey<LootTable> dan$useTreasureTable() {
        return CarpetDanSettings.onlyTreasure()
                ? BuiltInLootTables.FISHING_TREASURE
                : BuiltInLootTables.FISHING;
    }
}
