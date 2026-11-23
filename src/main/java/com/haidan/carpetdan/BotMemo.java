package com.haidan.carpetdan;

import carpet.helpers.EntityPlayerActionPack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

/**
 * One saved bot snapshot: where it stood, which way it looked, its game mode, and a copy of its
 * {@link EntityPlayerActionPack} (the Carpet structure that drives use/attack/move actions).
 */
public record BotMemo(ResourceKey<Level> dimension, double x, double y, double z, float yaw, float pitch,
                      GameType gameType, EntityPlayerActionPack snapshot) {
}
