package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.CarpetDanSettings;
import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

/**
 * Gates Unicode bot names at execution time.
 *
 * <p>The {@code /player} argument type is decided when the command tree is built, which happens
 * before Carpet applies the values saved in {@code carpet.conf}. Reading the rule at registration
 * time would therefore ignore a saved {@code false}. Instead the tree always accepts quoted
 * strings and this gate rejects Unicode names while {@code botChineseName} is off, so toggling
 * the rule takes effect immediately.</p>
 *
 * <p>Player chat commands are dispatched through {@link Commands#performCommand(ParseResults, String)},
 * while RCON / command blocks can go through {@link Commands#performPrefixedCommand(CommandSourceStack, String)};
 * both entry points are gated.</p>
 */
@Mixin(Commands.class)
public abstract class PlayerChineseNameGateMixin {
    @Inject(method = "performCommand", at = @At("HEAD"), cancellable = true)
    private void carpetdan$gatePlayerCommand(ParseResults<CommandSourceStack> parse, String command, CallbackInfo ci) {
        if (carpetdan$shouldReject(command, parse.getContext().getSource())) {
            ci.cancel();
        }
    }

    @Inject(method = "performPrefixedCommand", at = @At("HEAD"), cancellable = true)
    private void carpetdan$gatePrefixedCommand(CommandSourceStack source, String command, CallbackInfo ci) {
        if (carpetdan$shouldReject(command, source)) {
            ci.cancel();
        }
    }

    private static boolean carpetdan$shouldReject(String command, CommandSourceStack source) {
        if (CarpetDanSettings.botChineseNameEnabled()) {
            return false;
        }
        String text = command.startsWith("/") ? command.substring(1) : command;
        String trimmed = text.trim();
        int space = trimmed.indexOf(' ');
        String root = (space < 0 ? trimmed : trimmed.substring(0, space)).toLowerCase(Locale.ROOT);
        if (!"player".equals(root)) {
            return false;
        }
        String rest = space < 0 ? "" : trimmed.substring(space + 1);
        for (int i = 0; i < rest.length(); i++) {
            if (rest.charAt(i) > 0x7F) {
                source.sendFailure(Component.literal("botChineseName is disabled: quoted Unicode bot names are not allowed."));
                return true;
            }
        }
        return false;
    }
}
