package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import github.kasuminova.network.message.whitelist.FullWhiteListInfo;
import github.kasuminova.network.message.whitelist.WhiteListAddMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.ArrayList;
import java.util.List;

public class WhiteListAddCmd extends GroupCommandCL {
    public WhiteListAddCmd(ServerHelperCL cl) {
        super("申请白名单",
                1,
                0,
                new MessageChainBuilder()
                        .append("介绍：申请服务器白名单。").append(MiraiCodes.WRAP)
                        .append(String.format("例：%s申请白名单 PLAYER", KasumiNovaBot2.COMMAND_PREFIX))
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        String playerName = args.get(0);

        if (!MiscUtil.isValidUserName(playerName)) {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("非法用户名，ID 只能由字母、数字和下划线组成，且长度至少为 3 个字符，最多为 16 个字符。").build(),
                    event.getGroup());
            return;
        }

        FullWhiteListInfo fullWhiteListInfo = new FullWhiteListInfo(
                playerName,
                event.getSender().getId(),
                System.currentTimeMillis(),
                new ArrayList<>(0));

        cl.sendMessageToServer(new WhiteListAddMessage(fullWhiteListInfo), true);
    }
}
