package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.network.message.serverinfo.OnlineGetMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.List;

public class GetPlayersCmd extends GroupCommandCL {
    public GetPlayersCmd(ServerHelperCL cl) {
        super("在线",
                0,
                1,
                0,
                new MessageChainBuilder()
                        .append("介绍：获取服务器在线人数和所有在线的玩家状态信息。")
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        cl.sendMessageToServer(new OnlineGetMessage(true), true);
    }
}
