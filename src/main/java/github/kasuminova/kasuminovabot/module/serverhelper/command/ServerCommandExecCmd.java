package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.network.message.servercmd.CmdExecMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.List;

public class ServerCommandExecCmd extends GroupCommandCL {
    public ServerCommandExecCmd(ServerHelperCL cl) {
        super("EXEC",
                2,
                2,
                new MessageChainBuilder()
                        .append("介绍：向指定服务器执行指令。")
                        .append(String.format("例：%sEXEC SC1 p give Kasumi_Nova 114514", KasumiNovaBot2.COMMAND_PREFIX))
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        cl.sendMessageToServer(new CmdExecMessage(args.get(0), String.valueOf(event.getBot().getId()), args.get(1)), true);
    }
}
