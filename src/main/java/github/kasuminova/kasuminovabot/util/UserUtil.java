package github.kasuminova.kasuminovabot.util;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.data.BotData;

import java.util.Map;

public class UserUtil {
    public static boolean isSuperAdmin(long id) {
        return KasumiNovaBot2.SUPER_ADMIN.contains(id);
    }

    public static boolean isInBlackList(long id, long botId) {
        BotData.Data botData = BotData.INSTANCE.getBotData(botId);
        return botData.getCommandBlackList().contains(id);
    }
}
