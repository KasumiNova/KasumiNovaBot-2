package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.network.message.servercmd.CmdExecMessage;
import github.kasuminova.network.message.servercmd.GlobalCmdExecMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.List;

public class GlobalCommandExecCmd extends GroupCommandCL {
    public GlobalCommandExecCmd(ServerHelperCL cl) {
        super("EXEC_GLOBAL",
                1,
                2,
                new MessageChainBuilder()
                        .append("介绍：向所有服务器执行指令，需要管理员权限，不是所有指令返回的消息都能看到。").append(MiraiCodes.WRAP)
                        .append(String.format("例：%sEXEC_GLOBAL stop", KasumiNovaBot2.COMMAND_PREFIX))
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        cl.sendMessageToServer(new GlobalCmdExecMessage(String.valueOf(event.getBot().getId()), args.get(0)), true);
    }
}
