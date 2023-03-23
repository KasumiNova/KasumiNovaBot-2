package github.kasuminova.kasuminovabot.command;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.List;

public abstract class GroupCommand {
    public final String commandName;
    public final int minArgCount;
    public final int maxArgCount;
    public final MessageChain description;
    public final int permission;

    public GroupCommand(String commandName, int minArgCount, int maxArgCount, int permission, MessageChain description) {
        this.commandName = commandName;
        this.minArgCount = minArgCount;
        this.maxArgCount = maxArgCount;
        this.permission = permission;
        this.description = description;
    }

    public abstract void execute(GroupMessageEvent event, List<String> args);
}
