package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.tips.CustomTip;
import github.kasuminova.kasuminovabot.module.tips.TipManager;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;

public class ApproveTipCmd extends GroupCommand {

    public static final ApproveTipCmd INSTANCE = new ApproveTipCmd();

    private ApproveTipCmd() {
        super("审核随机提示",
                0,
                1,
                1,
                new MessageChainBuilder()
                        .append("指令介绍：通过提示审核列表中指定 ID 的提示，并加入到随机列表中，需要管理员权限。").append(MiraiCodes.WRAP)
                        .append(String.format("例：%s审核随机提示 514", KasumiNovaBot2.COMMAND_PREFIX)).append(MiraiCodes.WRAP)
                        .append("如果不输入参数则输出未审核的随机提示列表。")
                        .build()
        );
    }

    @Override
    public void execute(final GroupMessageEvent event, final List<String> args) {
        if (args.isEmpty()) {
            MessageChainBuilder msgBuilder = new MessageChainBuilder()
                    .append("当前正在审核的随机提示：");
            int counter = 0;
            for (final CustomTip customTip : TipManager.getTipStorage().getCheckingTips().values()) {
                if (counter >= 10) {
                    break;
                }
                msgBuilder.append(MiraiCodes.WRAP).append(customTip.getTip()).append(" —— ").append(customTip.getWho())
                        .append("(ID: ").append(String.valueOf(customTip.getId())).append(")");
                counter++;
            }
            MiscUtil.sendMessageToGroup(msgBuilder.build(), event.getGroup());
            return;
        }

        String arg = args.get(0);
        if (!MiscUtil.isNum(arg)) {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                            .append(new QuoteReply(event.getMessage()))
                            .append("非法参数 ").append(arg).append(MiraiCodes.WRAP)
                            .append("应为纯数字。")
                            .build(), event.getGroup());
            return;
        }

        int id = Integer.parseInt(arg);
        if (TipManager.approveTips(id)) {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("审核通过成功。")
                    .build(), event.getGroup());
        } else {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("审核通过失败，ID 不存在。")
                    .build(), event.getGroup());
        }
    }
}
