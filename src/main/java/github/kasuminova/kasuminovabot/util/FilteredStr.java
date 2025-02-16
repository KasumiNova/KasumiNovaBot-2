package github.kasuminova.kasuminovabot.util;

import java.util.List;

public record FilteredStr(String filteredString, List<String> matchedWords) {
    public boolean isFiltered() {
        return !matchedWords.isEmpty();
    }
}
