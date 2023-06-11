package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import github.kasuminova.network.message.whitelist.SearchMethod;
import github.kasuminova.network.message.whitelist.WhiteListRemoveMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;

public class WhiteListRemoveCmd extends GroupCommandCL implements SearchMethod {
    public WhiteListRemoveCmd(ServerHelperCL cl) {
        super("删除白名单",
                3,
                1,
                new MessageChainBuilder()
                        .append(String.format("用法：%s删除白名单 <QQ|ID> <用户 QQ|游戏名> <1|0> (目标是否为回收站，1 为是，0 为否)", KasumiNovaBot2.COMMAND_PREFIX))
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        String searchMethod = args.get(0);
        String userNameOrId = args.get(1);
        String deleteForeverStr = args.get(2);
        boolean deleteForever;

        if (MiscUtil.isNum(deleteForeverStr)) {
            int operation = Integer.parseInt(deleteForeverStr);
            if (operation == 0) {
                deleteForever = false;
            } else if (operation == 1) {
                deleteForever = true;
            } else {
                MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("非法参数 ").append(deleteForeverStr).append(MiraiCodes.WRAP)
                        .append("应为 <1|0>")
                        .build(), event.getGroup());

                return;
            }
        } else {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("非法参数 ").append(deleteForeverStr).append(MiraiCodes.WRAP)
                    .append("应为 <1|0>")
                    .build(), event.getGroup());

            return;
        }

        switch (SearchMethod.getIntWithString(searchMethod)) {
            case SEARCH_ID: {
                cl.sendMessageToServer(new WhiteListRemoveMessage(userNameOrId, deleteForever), true);
                break;
            }
            case SEARCH_QQ: {
                if (MiscUtil.isNum(userNameOrId)) {
                    cl.sendMessageToServer(new WhiteListRemoveMessage(Long.parseLong(userNameOrId), deleteForever), true);
                } else {
                    MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                                    .append(new QuoteReply(event.getMessage()))
                                    .append("非法参数 ").append(userNameOrId).append(MiraiCodes.WRAP)
                                    .append("应为纯数字。").build(),
                            event.getGroup());
                }
                break;
            }
            default: MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                            .append(new QuoteReply(event.getMessage()))
                            .append("非法参数 ").append(searchMethod).append(MiraiCodes.WRAP)
                            .append("应为 `QQ` 或 `ID`").build(),
                    event.getGroup());
        }
    }
}
