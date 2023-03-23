package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.command.GroupCommand;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import net.mamoe.mirai.message.data.MessageChain;

public abstract class GroupCommandCL extends GroupCommand {
    public final ServerHelperCL cl;

    public GroupCommandCL(String commandName, int minArgCount, int maxArgCount, int permission, MessageChain description, ServerHelperCL cl) {
        super(commandName, minArgCount, maxArgCount, permission, description);
        this.cl = cl;
    }

    public GroupCommandCL(String commandName, int argCount, int permission, MessageChain description, ServerHelperCL cl) {
        super(commandName, argCount, argCount, permission, description);
        this.cl = cl;
    }
}
