package github.kasuminova.kasuminovabot.module.serverhelper.command;

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

public class WhiteListForceAddCmd extends GroupCommandCL {
    public WhiteListForceAddCmd(ServerHelperCL cl) {
        super("强制申请白名单",
                2,
                1,
                new MessageChainBuilder()
                        .append("介绍：强制申请服务器白名单，无视用户名检查，需要管理员权限。")
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        String id = args.get(0);
        String playerName = args.get(1);

        if (!MiscUtil.isNum(id)) {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                            .append(new QuoteReply(event.getMessage()))
                            .append("非法参数 ").append(id).append(MiraiCodes.WRAP)
                            .append("应为纯数字。")
                            .build(),
                    event.getGroup());
            return;
        }

        FullWhiteListInfo fullWhiteListInfo = new FullWhiteListInfo(
                playerName,
                Long.parseLong(id),
                System.currentTimeMillis(),
                new ArrayList<>(0));

        cl.sendMessageToServer(new WhiteListAddMessage(fullWhiteListInfo), true);
    }
}
