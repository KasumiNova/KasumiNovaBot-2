package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.network.message.playercmd.KickMeMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.List;

public class KickMeCmd extends GroupCommandCL {
    public KickMeCmd(ServerHelperCL cl) {
        super("踹我",
                0,
                0,
                new MessageChainBuilder()
                        .append("介绍：将你的 QQ 绑定的白名单玩家踹出服务器，以解决客户端掉线但是玩家依然在服务器内的问题。")
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        cl.sendMessageToServer(new KickMeMessage(String.valueOf(event.getSender().getId())), true);
    }
}
