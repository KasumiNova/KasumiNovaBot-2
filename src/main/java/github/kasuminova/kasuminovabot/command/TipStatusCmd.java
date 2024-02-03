package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.module.tips.CustomTip;
import github.kasuminova.kasuminovabot.module.tips.TipManager;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.*;

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
        Map<Long, Integer> userTipCounter = new TreeMap<>(Comparator.reverseOrder());
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

        int count = 0;
        for (final Map.Entry<Long, Integer> entry : userTipCounter.entrySet()) {
            if (count > 5) {
                break;
            }

            msgBuilder.append(MiraiCodes.WRAP)
                    .append(userNick.get(entry.getKey()))
                    .append(" (").append(String.valueOf(entry.getKey())).append(")")
                    .append("：").append(String.valueOf(entry.getValue())).append(" 条");
            count++;
        }

        MiscUtil.sendMessageToGroup(msgBuilder.build(), event.getGroup());
    }
}
