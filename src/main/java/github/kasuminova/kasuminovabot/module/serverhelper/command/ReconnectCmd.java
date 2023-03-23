package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;

public class ReconnectCmd extends GroupCommandCL {
    public ReconnectCmd(ServerHelperCL cl) {
        super("重新连接内部服务器",
                0,
                1,
                new MessageChainBuilder()
                        .append("介绍：重新连接插件服务器，需要管理员权限。")
                        .build(),
                cl);
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        ServerHelperCL.EXECUTOR.execute(() -> {
            try {
                cl.connect();
                MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("连接成功。")
                        .build(), event.getGroup());
            } catch (Exception e) {
                KasumiNovaBot2.INSTANCE.logger.warning(e);
                MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("连接服务器失败，请检查后台日志。")
                        .build(), event.getGroup());
            }
        });
    }
}
