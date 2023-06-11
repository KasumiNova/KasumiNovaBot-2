package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.network.message.playercmd.PlayerCmdExecMessage;
import github.kasuminova.network.message.servercmd.CmdExecMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.List;

public class PlayerCommandExecCmd extends GroupCommandCL {
    public PlayerCommandExecCmd(ServerHelperCL cl) {
        super("执行指令",
                1,
                0,
                new MessageChainBuilder()
                        .append("介绍：使用你的 QQ 所绑定的白名单账号执行指令，玩家需要在线，指令返回的消息只能在游戏内看到。").append(MiraiCodes.WRAP)
                        .append(String.format("例：%s执行指令 suicide", KasumiNovaBot2.COMMAND_PREFIX))
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        cl.sendMessageToServer(new PlayerCmdExecMessage(
                String.valueOf(event.getSender().getId()),
                "",
                String.valueOf(event.getBot().getId()),
                args.get(0)), true);
    }
}
