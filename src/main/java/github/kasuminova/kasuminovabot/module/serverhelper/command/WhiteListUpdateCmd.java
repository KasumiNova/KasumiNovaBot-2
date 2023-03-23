package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import github.kasuminova.network.message.whitelist.SearchMethod;
import github.kasuminova.network.message.whitelist.WhiteListUpdateMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;

public class WhiteListUpdateCmd extends GroupCommandCL implements SearchMethod {
    public WhiteListUpdateCmd(ServerHelperCL cl) {
        super("修改白名单",
                3,
                1,
                new MessageChainBuilder()
                        .append(String.format("用法：%s修改白名单 <QQ|USERNAME> <用户 QQ|旧游戏名> <用户 QQ|新游戏名>", KasumiNovaBot2.COMMAND_PREFIX))
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        String searchMethod = args.get(0);
        String oldInfo = args.get(1);
        String newInfo = args.get(2);

        switch (SearchMethod.getIntWithString(searchMethod)) {
            case SEARCH_USERNAME: {
                cl.sendMessageToServer(new WhiteListUpdateMessage(oldInfo, newInfo), true);
                break;
            }
            case SEARCH_QQ: {
                if (MiscUtil.isNum(oldInfo)) {
                    if (MiscUtil.isNum(newInfo)) {
                        cl.sendMessageToServer(new WhiteListUpdateMessage(Long.parseLong(oldInfo), Long.parseLong(newInfo)), true);
                    } else {
                        MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                                        .append(new QuoteReply(event.getMessage()))
                                        .append("非法参数 ").append(newInfo).append(MiraiCodes.WRAP)
                                        .append("应为纯数字。").build(),
                                event.getGroup());
                    }
                } else {
                    MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                                    .append(new QuoteReply(event.getMessage()))
                                    .append("非法参数 ").append(oldInfo).append(MiraiCodes.WRAP)
                                    .append("应为纯数字。").build(),
                            event.getGroup());
                }
                break;
            }
            default: MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                            .append(new QuoteReply(event.getMessage()))
                            .append("非法参数 ").append(searchMethod).append(MiraiCodes.WRAP)
                            .append("应为 `QQ` 或 `USERNAME`").build(),
                    event.getGroup());
        }
    }
}
