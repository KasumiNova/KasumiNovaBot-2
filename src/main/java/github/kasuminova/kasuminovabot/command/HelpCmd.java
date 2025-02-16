package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import github.kasuminova.kasuminovabot.util.UserUtil;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpCmd extends GroupCommand {
    public static final HelpCmd INSTANCE = new HelpCmd();

    private HelpCmd() {
        super("帮助",
                0,
                1,
                0,
                new MessageChainBuilder()
                        .append("指令介绍：获取当前群聊可用指令列表")
                        .build()
        );
    }

    @Override
    public void execute(GroupMessageEvent event, List<String> args) {
        MessageChainBuilder builder = new MessageChainBuilder().append("指令列表：").append(MiraiCodes.WRAP);

        Member sender = event.getSender();
        int permissionLvl = sender.getPermission().getLevel();

        Collection<GroupCommand> globalCommands = KasumiNovaBot2.INSTANCE.getRegisteredCommands().values();
        for (GroupCommand cmd : globalCommands) {
            if (!(cmd.permission > permissionLvl) || UserUtil.isSuperAdmin(sender.getId())) {
                builder.append(KasumiNovaBot2.COMMAND_PREFIX).append(cmd.commandName).append(MiraiCodes.WRAP);
            }
        }

        Map<String, GroupCommand> privateGroupCmds = KasumiNovaBot2.INSTANCE.getPrivateGroupCommands().get(String.valueOf(event.getGroup().getId()));
        if (privateGroupCmds != null) {
            builder.append("本群额外可使用的指令：").append(MiraiCodes.WRAP);
            for (GroupCommand cmd : privateGroupCmds.values()) {
                if (!(cmd.permission > permissionLvl) || UserUtil.isSuperAdmin(sender.getId())) {
                    builder.append(KasumiNovaBot2.COMMAND_PREFIX).append(cmd.commandName).append(MiraiCodes.WRAP);
                }
            }
        }

        MiscUtil.sendMessageToGroup(builder.build(), event.getGroup());
    }
}
