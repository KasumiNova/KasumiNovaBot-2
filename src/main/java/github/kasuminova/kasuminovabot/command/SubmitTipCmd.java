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

public class SubmitTipCmd extends GroupCommand {

    public static final SubmitTipCmd INSTANCE = new SubmitTipCmd();

    private SubmitTipCmd() {
        super("投稿随机提示",
                1,
                60,
                0,
                new MessageChainBuilder()
                        .append("指令介绍：为随机一言投稿新的提示！").append(MiraiCodes.WRAP)
                        .append(String.format("例：%s投稿随机提示 你是一个一个一个啊啊啊啊啊啊", KasumiNovaBot2.COMMAND_PREFIX))
                        .build()
        );
    }

    @Override
    public void execute(final GroupMessageEvent event, final List<String> args) {
        StringBuilder tipBuilder = new StringBuilder();
        String tip;
        for (int i = 0; i < args.size(); i++) {
            final String arg = args.get(i);
            tipBuilder.append(arg);
            if (i + 1 < args.size()) {
                tipBuilder.append(" ");
            }
        }
        tip = tipBuilder.toString();

        if (tip.length() < 5 || tip.length() > 60) {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("长度不得小于 5 个字符，并且不得大于 60 个字符。").build(), event.getGroup());
            return;
        }

        Member sender = event.getSender();
        CustomTip customTip = new CustomTip(TipManager.nextId(), tip, sender.getId(), sender.getNick(), System.currentTimeMillis());

        if (sender.getPermission().getLevel() >= 1) {
            TipManager.addCheckedTip(customTip);
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("添加成功！管理员无需审核。").build(), event.getGroup());
        } else {
            TipManager.addCheckingTip(customTip);
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("提交成功！请等待管理员审核（ID: ").append(String.valueOf(customTip.getId())).append("）。")
                    .build(), event.getGroup());
        }
    }
}
