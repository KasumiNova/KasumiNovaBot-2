package github.kasuminova.kasuminovabot.module.serverhelper.command.console;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JSimpleCommand;

public class ReloadCLCmd extends JSimpleCommand {
    public ReloadCLCmd() {
        super(KasumiNovaBot2.INSTANCE, "kb2_reload_cl");
    }

    @Handler
    public void reloadCL(CommandContext context) {
        KasumiNovaBot2.INSTANCE.logger.info("Reloading All ServerHelperCL...");

        ServerHelperCL.EXECUTOR.execute(() -> {
            ServerHelperCL.unloadAll();
            ServerHelperCL.loadAll();
        });
    }
}
