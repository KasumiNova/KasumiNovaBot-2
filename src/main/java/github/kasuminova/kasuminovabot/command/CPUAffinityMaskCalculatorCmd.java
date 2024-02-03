package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.command.exception.CommandParseException;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class CPUAffinityMaskCalculatorCmd extends GroupCommand {

    public static final CPUAffinityMaskCalculatorCmd INSTANCE = new CPUAffinityMaskCalculatorCmd();

    private CPUAffinityMaskCalculatorCmd() {
        super("CPU_AFFINITY_MASK",
                1,
                64,
                0,
                new MessageChainBuilder()
                        .append("介绍：计算 Windows CMD 中的 /affinity 参数的 CPU 掩码。").append(MiraiCodes.WRAP)
                        .append(String.format("例：%sCPU_AFFINITY_MASK 1-9 24", KasumiNovaBot2.COMMAND_PREFIX)).append(MiraiCodes.WRAP)
                        .append("多个 CPU 使用空格分隔，支持连续计算（1-9，7-10 等），最多可计算 64 个 CPU。").append(MiraiCodes.WRAP)
                        .append("如果传入的参数为 16 进制码，则计算转换前的 CPU。").append(MiraiCodes.WRAP)
                        .append(String.format("例：%sCPU_AFFINITY_MASK 0xFFAAFF", KasumiNovaBot2.COMMAND_PREFIX))
                        .build()
        );
    }

    @Override
    public void execute(final GroupMessageEvent event, final List<String> args) {
        long cpuMask = -1;
        if (args.size() == 1) {
            cpuMask = tryParseHexLong(args.get(0));
        }

        Set<Integer> cpuSet;
        if (cpuMask != -1) {
            cpuSet = getCPUsFromMask(cpuMask);
        } else {
            try {
                cpuSet = splitArgToIntList(args, event);
            } catch (CommandParseException e) {
                MiscUtil.sendMessageToGroup(e.getErrorMessage(), event.getGroup());
                return;
            }
        }

        MessageChainBuilder msg = new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append("计算结果：").append(MiraiCodes.WRAP)
                .append("CPU: ");

        int size = 0;
        for (Integer cpu : cpuSet) {
            msg.append(String.valueOf(cpu));
            if (size + 1 < cpuSet.size()) {
                msg.append(",");
            }
            size++;
        }
        msg.append(MiraiCodes.WRAP);

        msg.append("结果：0x").append(calculateMask(cpuSet).toUpperCase());

        MiscUtil.sendMessageToGroup(msg.build(), event.getGroup());
    }

    private static long tryParseHexLong(String arg) {
        if (arg.startsWith("0x")) {
            try {
                return Long.parseLong(arg.substring(2), 16);
            } catch (Exception ignored) {
            }
        }
        return -1;
    }

    private static String calculateMask(Set<Integer> cpuSet) {
        long cpuMask = 0;
        for (final int cpu : cpuSet) {
            cpuMask |= (1L << cpu);
        }
        return Long.toHexString(cpuMask);
    }

    private static Set<Integer> splitArgToIntList(List<String> args, final GroupMessageEvent event) throws CommandParseException {
        Set<Integer> list = new LinkedHashSet<>();

        for (final String s : args) {
            if (!s.contains("-")) {
                list.add(parseCPUValue(s, event));
                continue;
            }

            String[] leftAndRight = s.split("-");
            if (leftAndRight.length != 2) {
                throw new CommandParseException(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("无法解析参数 ").append(s).append(MiraiCodes.WRAP)
                        .append("参考示例：0 - 9（<NUMBER> - <NUMBER>）")
                        .build());
            }
            String leftStr = leftAndRight[0];
            String rightStr = leftAndRight[1];
            int left = parseCPUValue(leftStr, event);
            int right = parseCPUValue(rightStr, event);

            if (left > right) {
                throw new CommandParseException(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("无法解析参数 ").append(s).append(MiraiCodes.WRAP)
                        .append(String.valueOf(left)).append(" 不能大于 ").append(String.valueOf(right))
                        .build());
            }

            IntStream.rangeClosed(left, right).forEach(list::add);
        }

        return list;
    }

    private static Set<Integer> getCPUsFromMask(long cpuMask) {
        Set<Integer> cpuSet = new LinkedHashSet<>();

        for (int i = 0; i < 64; i++) {
            if ((cpuMask & (1L << i)) != 0) {
                cpuSet.add(i);
            }
        }

        return cpuSet;
    }

    private static int parseCPUValue(String str, final GroupMessageEvent event) throws CommandParseException {
        if (!MiscUtil.isNum(str)) {
            throw new CommandParseException(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("非法参数 ").append(str).append(MiraiCodes.WRAP)
                    .append("应为纯数字。")
                    .build());
        }
        int value = Integer.parseInt(str);
        if (!isInCalculateRange(value)) {
            throw new CommandParseException(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("非法参数 ").append(str).append(MiraiCodes.WRAP)
                    .append("数值不得超过 63，且不得小于 0。")
                    .build());
        }
        return value;
    }

    private static boolean isInCalculateRange(int num) {
        return num >= 0 && num <= 63;
    }

}
