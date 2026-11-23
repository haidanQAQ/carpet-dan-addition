package com.haidan.carpetdan;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Shared helper that wraps non-ASCII suggestion text in double quotes.
 *
 * <p>Brigadier only accepts ASCII characters in unquoted arguments, so a Chinese bot name
 * inserted by tab completion would fail to parse. Quoting the suggestion text means the
 * client inserts {@code "测试"} instead, which parses normally.</p>
 */
public final class SuggestionText {
    private SuggestionText() {
    }

    public static boolean needsQuotes(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        char first = text.charAt(0);
        if (first == '@' || first == '"' || first == '\'') {
            return false;
        }
        if (text.indexOf('"') >= 0 || text.indexOf('\'') >= 0) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) > 0x7F) {
                return true;
            }
        }
        return false;
    }

    public static String quoteIfNeeded(String text) {
        return needsQuotes(text) ? "\"" + text + "\"" : text;
    }

    public static Collection<String> quoteAll(Collection<String> names) {
        if (names == null || names.isEmpty()) {
            return names;
        }
        List<String> quoted = new ArrayList<>(names.size());
        for (String name : names) {
            quoted.add(quoteIfNeeded(name));
        }
        return quoted;
    }

    public static Suggestions quoteSuggestions(Suggestions suggestions) {
        if (suggestions == null) {
            return null;
        }
        List<Suggestion> quoted = new ArrayList<>(suggestions.getList().size());
        for (Suggestion suggestion : suggestions.getList()) {
            String text = suggestion.getText();
            if (needsQuotes(text)) {
                quoted.add(new Suggestion(suggestion.getRange(), "\"" + text + "\"", suggestion.getTooltip()));
            } else {
                quoted.add(suggestion);
            }
        }
        return new Suggestions(suggestions.getRange(), quoted);
    }
}
