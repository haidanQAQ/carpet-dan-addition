package com.haidan.carpetdan.mixin;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.server.network.ServerCommandSuggestionsProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Makes tab completion wrap non-ASCII suggestions in double quotes.
 *
 * <p>Brigadier only accepts ASCII characters in unquoted arguments, so a Chinese bot name
 * inserted by tab completion (which inserts the raw suggestion text) would fail to parse.
 * Quoting the suggestion text means the client inserts {@code "测试"} instead of
 * {@code 测试}, which parses normally. Suggestions already inside a quoted string are left
 * alone so that typing a quote and then tabbing still works.</p>
 */
@Mixin(ServerCommandSuggestionsProvider.class)
public abstract class SuggestionQuotingMixin {
    @Inject(method = "request", at = @At("RETURN"), cancellable = true)
    private void carpetdan$quoteUnicodeSuggestions(String command, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
        CompletableFuture<Suggestions> future = cir.getReturnValue();
        if (future == null) {
            return;
        }
        cir.setReturnValue(future.thenApply(suggestions -> quoteSuggestions(command, suggestions)));
    }

    private static Suggestions quoteSuggestions(String command, Suggestions suggestions) {
        List<Suggestion> quoted = new ArrayList<>(suggestions.getList().size());
        for (Suggestion suggestion : suggestions.getList()) {
            String text = suggestion.getText();
            if (needsQuotes(command, suggestion.getRange(), text)) {
                quoted.add(new Suggestion(suggestion.getRange(), "\"" + text + "\"", suggestion.getTooltip()));
            } else {
                quoted.add(suggestion);
            }
        }
        return new Suggestions(suggestions.getRange(), quoted);
    }

    private static boolean needsQuotes(String command, StringRange range, String text) {
        if (text.isEmpty()) {
            return false;
        }
        char first = text.charAt(0);
        if (first == '@' || first == '"' || first == '\'') {
            return false;
        }
        if (text.indexOf('"') >= 0 || text.indexOf('\'') >= 0) {
            return false;
        }
        boolean unicode = false;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) > 0x7F) {
                unicode = true;
                break;
            }
        }
        if (!unicode) {
            return false;
        }
        int start = range.getStart();
        if (command != null && start > 0) {
            char previous = command.charAt(start - 1);
            if (previous == '"' || previous == '\'') {
                return false;
            }
        }
        return true;
    }
}
