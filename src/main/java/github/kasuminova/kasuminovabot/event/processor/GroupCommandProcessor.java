package github.kasuminova.kasuminovabot.event.processor;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.command.Commands;
import github.kasuminova.kasuminovabot.command.GroupCommand;
import github.kasuminova.kasuminovabot.event.listener.GroupMessageListener;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import github.kasuminova.kasuminovabot.util.UserUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupCommandProcessor extends GroupMessageEventProcessor {
    private final Map<String, GroupCommand> registeredGlobalCommands;
    private final Map<String, Map<String, GroupCommand>> groupPrivateCommands;

    public GroupCommandProcessor(
            Map<String, GroupCommand> registeredGlobalCommands,
            Map<String, Map<String, GroupCommand>> groupPrivateCommands,
            GroupMessageListener messageListener
    ) {
        super(messageListener);
        this.registeredGlobalCommands = registeredGlobalCommands;
        this.groupPrivateCommands = groupPrivateCommands;
    }

    public static boolean processCommand(GroupMessageEvent event, Map<String, GroupCommand> commands) {
        String msg = event.getMessage().serializeToMiraiCode();

        String commandStr = Commands.getCommand(msg);
        GroupCommand command = commands.get(commandStr);

        if (command == null) {
            KasumiNovaBot2.INSTANCE.logger.warning("无效指令 " + commandStr);
            return false;
        }

        Member sender = event.getSender();
        Group group = event.getGroup();
        Bot bot = event.getBot();

        if (!UserUtil.isSuperAdmin(sender.getId())) {
            if (command.permission > sender.getPermission().getLevel() || UserUtil.isInBlackList(sender.getId(), bot.getId())) {
                MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("你没有权限，或是在机器人黑名单内。")
                        .build(), group
                );
                return false;
            }
        }

        ArrayList<String> args = Commands.getArgs(msg, command.maxArgCount);
        if (command.minArgCount != -1) {
            if (args.size() < command.minArgCount) {
                MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append(String.format("指令无效，因为输入的参数不足 (%s 参数 / 需要 %s 参数)。", args.size(), command.minArgCount)).append(MiraiCodes.WRAP)
                        .append(command.description)
                        .build(), group
                );
                return false;
            }
        }

        command.execute(event, args);
        KasumiNovaBot2.INSTANCE.logger.info("已执行指令 " + commandStr);

        return true;
    }

    @Override
    public EventProcessor<GroupMessageEvent> getMessageProcessor() {
        return event -> {
            String msg = event.getMessage().serializeToMiraiCode();
            if (!Commands.isCommand(msg)) return false;

            long groupId = event.getGroup().getId();

            Map<String, GroupCommand> groupPrivateCommand = groupPrivateCommands.get(String.valueOf(groupId));
            if (groupPrivateCommand != null) {
                if (processCommand(event, groupPrivateCommand)) {
                    return true;
                }
            }

            return processCommand(event, registeredGlobalCommands);
        };
    }
}
