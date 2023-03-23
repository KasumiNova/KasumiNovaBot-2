package github.kasuminova.kasuminovabot.module.serverhelper.command.console;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JSimpleCommand;

public class ReconnectCLCmd extends JSimpleCommand {
    public ReconnectCLCmd() {
        super(KasumiNovaBot2.INSTANCE, "kb2_reconnect");
    }

    @Handler
    public void reconnect(CommandContext context, String groupId) {
        for (ServerHelperCL serverHelperCL : ServerHelperCL.CL_LIST) {
            String groupIdStr = String.valueOf(serverHelperCL.getConfig().getGroupID());
            if (!groupId.equals(groupIdStr)) {
                continue;
            }

            ServerHelperCL.EXECUTOR.execute(() -> {
                try {
                    serverHelperCL.connect();
                } catch (Exception e) {
                    KasumiNovaBot2.INSTANCE.logger.warning("连接至插件服务器失败！", e);
                }
            });
            return;
        }

        KasumiNovaBot2.INSTANCE.logger.warning("未找到对应群聊的实例！");
    }
}
