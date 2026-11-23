package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.SuggestionText;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Client half of the Chinese-name completion fix.
 *
 * <p>Player-name suggestions for commands such as {@code /tp} are computed on the client,
 * so the server cannot quote them. Wrapping the names here makes tab completion insert
 * {@code "测试"} instead of {@code 测试}. Server-provided suggestions (Carpet's
 * {@code /player}) are covered by {@link SuggestionQuotingMixin} on the server side.</p>
 */
@Mixin(ClientSuggestionProvider.class)
public abstract class ClientSuggestionQuotingMixin {
    @Inject(method = "getOnlinePlayerNames", at = @At("RETURN"), cancellable = true, require = 0)
    private void carpetdan$quotePlayerNames(CallbackInfoReturnable<Collection<String>> cir) {
        cir.setReturnValue(SuggestionText.quoteAll(cir.getReturnValue()));
    }

    @Inject(method = "customSuggestion", at = @At("RETURN"), cancellable = true, require = 0)
    private void carpetdan$quoteCustomSuggestions(CommandContext<?> context, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
        CompletableFuture<Suggestions> future = cir.getReturnValue();
        if (future != null) {
            cir.setReturnValue(future.thenApply(SuggestionText::quoteSuggestions));
        }
    }
}
