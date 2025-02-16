package github.kasuminova.kasuminovabot.util;

import java.util.ArrayList;
import java.util.List;

public class ChatFilter {

    public static FilteredStr filterChat(final String originalChat,
                                         final List<String> inappropriateWordsCN,
                                         final List<String> inappropriateWordsEN
                                         ) {
        String filteredChat = originalChat;
        // 过滤消息中的标点符号和英文字符，保留中文字符
        String userChat = originalChat.replaceAll("[^\\u4E00-\\u9FFF]", "");

        // 进行匹配和过滤
        List<String> matchedWords = new ArrayList<>();
        for (final String inappropriateWord : inappropriateWordsCN) {
            if (userChat.contains(inappropriateWord)) {
                matchedWords.add(inappropriateWord);
                filteredChat = filteredChat.replace(inappropriateWord, "");
            }
        }
        userChat = originalChat.replaceAll("[^\\x00-\\x7F]", "");
        for (final String inappropriateWord : inappropriateWordsEN) {
            if (userChat.contains(inappropriateWord)) {
                matchedWords.add(inappropriateWord);
                filteredChat = filteredChat.replace(inappropriateWord, "");
            }
        }

        return new FilteredStr(filteredChat, matchedWords);
    }

}
