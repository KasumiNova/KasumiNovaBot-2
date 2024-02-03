package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.tips.CustomTip;
import github.kasuminova.kasuminovabot.module.tips.TipManager;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;

public class RemoveTipCmd extends GroupCommand {

    public static final RemoveTipCmd INSTANCE = new RemoveTipCmd();

    private RemoveTipCmd() {
        super("删除随机提示",
                1,
                1,
                1,
                new MessageChainBuilder()
                        .append("指令介绍：删除随机一言中指定 ID 的提示，需要管理员权限。").append(MiraiCodes.WRAP)
                        .append(String.format("例：%s删除随机提示 114", KasumiNovaBot2.COMMAND_PREFIX))
                        .build()
        );
    }

    @Override
    public void execute(final GroupMessageEvent event, final List<String> args) {
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
        if (TipManager.removeTip(id)) {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("删除成功。")
                    .build(), event.getGroup());
        } else {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("删除失败，ID 不存在。")
                    .build(), event.getGroup());
        }
    }
}
