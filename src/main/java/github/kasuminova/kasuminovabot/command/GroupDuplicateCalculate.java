package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.util.FileUtil;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupDuplicateCalculate extends GroupCommand {

    public static final GroupDuplicateCalculate INSTANCE = new GroupDuplicateCalculate();

    private GroupDuplicateCalculate() {
        super("测试",
                0,
                1,
                2,
                new MessageChainBuilder()
                        .append("指令介绍：仅供测试。")
                        .build()
        );
    }

    @Override
    public void execute(final GroupMessageEvent event, final List<String> args) {
        Group source = event.getBot().getGroup(0); // TODO
        Group target = event.getBot().getGroup(0); // TODO

        MessageChainBuilder msg = new MessageChainBuilder();
        if (source == null || target == null) {
            KasumiNovaBot2.INSTANCE.logger.warning("Test failed: Group not found.");
            msg.append("测试失败。");
            MiscUtil.sendMessageToGroup(msg.build(), event.getGroup());
            return;
        }

        List<Long> dupMembers = new ArrayList<>();
        ContactList<NormalMember> targetMembers = target.getMembers();
        for (final NormalMember member : source.getMembers()) {
            NormalMember dup = targetMembers.get(member.getId());
            if (dup != null) {
                dupMembers.add(member.getId());
            }
        }

        String sb = dupMembers.stream().map(id -> id + "\n").collect(Collectors.joining());

        try {
            File file = KasumiNovaBot2.INSTANCE.resolveDataFile("dupMembers.txt");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            RandomAccessFile ras = new RandomAccessFile(file, "rws");
            FileUtil.writeStringToFile(ras, sb);
            ras.close();
            msg.append("成功。");
        } catch (IOException e) {
            msg.append("测试失败。");
        }

        MiscUtil.sendMessageToGroup(msg.build(), event.getGroup());
    }

}
