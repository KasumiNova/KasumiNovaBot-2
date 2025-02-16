package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.module.tips.CustomTip;
import github.kasuminova.kasuminovabot.module.tips.TipManager;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TipStatusCmd extends GroupCommand {

    public static final TipStatusCmd INSTANCE = new TipStatusCmd();

    private TipStatusCmd() {
        super("随机提示状态",
                0,
                0,
                0,
                new MessageChainBuilder()
                        .append("指令介绍：输出随机提示的统计信息。").append(MiraiCodes.WRAP)
                        .build()
        );
    }

    @Override
    public void execute(final GroupMessageEvent event, final List<String> args) {
        List<CustomTip> indexedTips = TipManager.getIndexedTips();
        Map<Long, String> userNick = new HashMap<>();
        Map<Long, Integer> userTipCounter = new HashMap<>();
        for (final CustomTip indexedTip : indexedTips) {
            userTipCounter.compute(indexedTip.getSubmitter(), (_k, v) -> {
                if (v == null) {
                    return 1;
                }
                return v + 1;
            });
            userNick.put(indexedTip.getSubmitter(), indexedTip.getWho());
        }

        MessageChainBuilder msgBuilder = new MessageChainBuilder()
                .append("当前总共有 ").append(String.valueOf(indexedTips.size())).append(" 条已审核的随机提示。")
                .append(MiraiCodes.WRAP).append("提交排行榜：");

        final int[] current = {0};
        userTipCounter.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .forEach(entry -> {
                    current[0]++;
                    msgBuilder.append(MiraiCodes.WRAP)
                            .append(String.valueOf(current[0])).append(".")
                            .append(userNick.get(entry.getKey()))
                            .append(" (").append(String.valueOf(entry.getKey())).append(")")
                            .append("：").append(String.valueOf(entry.getValue())).append(" 条");
                });
        MiscUtil.sendMessageToGroup(msgBuilder.build(), event.getGroup());
    }
}
