package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import github.kasuminova.network.message.whitelist.SearchMethod;
import github.kasuminova.network.message.whitelist.WhiteListGetMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;

public class WhiteListGetCmd extends GroupCommandCL implements SearchMethod {
    public WhiteListGetCmd(ServerHelperCL cl) {
        super("查询白名单",
                2,
                0,
                new MessageChainBuilder()
                        .append(String.format("用法：%s查询白名单 <QQ|ID> <群聊 QQ|游戏名>", KasumiNovaBot2.COMMAND_PREFIX))
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        String searchMethod = args.get(0);
        switch (SearchMethod.getIntWithString(searchMethod)) {
            case SEARCH_ID: {
                cl.sendMessageToServer(new WhiteListGetMessage(args.get(1)), true);
                break;
            }
            case SEARCH_QQ: {
                String id = args.get(1);
                if (MiscUtil.isNum(id)) {
                    cl.sendMessageToServer(new WhiteListGetMessage(Long.parseLong(id)), true);
                } else {
                    MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                                    .append(new QuoteReply(event.getMessage()))
                                    .append("非法参数 ").append(id).append(MiraiCodes.WRAP)
                                    .append("应为纯数字。").build(),
                            event.getGroup());
                }
                break;
            }
            default: {
                MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                                .append(new QuoteReply(event.getMessage()))
                                .append("非法参数 ").append(searchMethod).append(MiraiCodes.WRAP)
                                .append("应为 `QQ` 或 `ID`").build(),
                        event.getGroup());
            }
        }
    }
}
