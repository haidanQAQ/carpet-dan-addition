package com.haidan.carpetdan.mixin;

import com.mojang.brigadier.arguments.StringArgumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Carpet parses the fake player name with {@link StringArgumentType#word()}, which only accepts
 * {@code [a-zA-Z0-9_]}. Swap it for {@link StringArgumentType#string()} so Unicode names such as
 * {@code 测试} can be used in {@code /player <name> spawn} and the other player subcommands.
 *
 * <p>The replacement is still a vanilla Brigadier argument type ({@code brigadier:string} with
 * {@code quotable_phrase}), so vanilla clients receive a command tree they already understand.
 */
@Mixin(targets = "carpet.commands.PlayerCommand")
public class PlayerCommandMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("carpet-dan-addition/PlayerCommandMixin");

    @Redirect(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/arguments/StringArgumentType;word()Lcom/mojang/brigadier/arguments/StringArgumentType;"
            )
    )
    private static StringArgumentType dan$allowUnicodePlayerNames() {
        LOGGER.info("swapped /player name argument from word() to string() for Unicode names");
        return StringArgumentType.string();
    }
}
