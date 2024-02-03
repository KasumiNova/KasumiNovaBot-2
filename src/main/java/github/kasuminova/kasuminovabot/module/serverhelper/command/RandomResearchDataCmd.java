package github.kasuminova.kasuminovabot.module.serverhelper.command;

import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.kasuminovabot.module.serverhelper.hypernet.NovaEngUtils;
import github.kasuminova.kasuminovabot.module.serverhelper.hypernet.ResearchCognitionData;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.List;
import java.util.Random;

public class RandomResearchDataCmd extends GroupCommandCL {
    private static final Random RD = new Random();

    private long lastExecuted = 0;

    public RandomResearchDataCmd(ServerHelperCL cl) {
        super("随机研究小作文",
                0,
                0,
                new MessageChainBuilder()
                        .append("介绍：获取随机研究小作文，从 HyperNet 网络终端获取（冷却时间 3 分钟）。")
                        .build(),
                cl);
    }

    @Override
    public void execute(final GroupMessageEvent event, final List<String> args) {
        long current = System.currentTimeMillis();
        if (lastExecuted + (3 * 60 * 1000) > current) {
            return;
        }

        List<ResearchCognitionData> dataList = cl.getStoredResearchData().getDataList();
        if (dataList.isEmpty()) {
            return;
        }

        ResearchCognitionData data = dataList.get(RD.nextInt(dataList.size() - 1));

        boolean locked = RD.nextBoolean();
        List<String> desc = locked ? data.getDescriptions() : data.getUnlockedDescriptions();
        MessageChain descChain = stringListToChain(desc);

        MessageChain msg = new MessageChainBuilder()
                .append(data.getResearchName()).append(MiraiCodes.WRAP)
                .append("科技等级：").append(String.valueOf(data.getTechLevel()))
                .append(MiraiCodes.WRAP)
                .append(locked ? "研究状态：已锁定" : "研究状态：已解锁")
                .append(MiraiCodes.WRAP)
                .append(descChain)
                .append(MiraiCodes.WRAP)
                .append("前置研究：").append(dependenciesToChain(data.getDependencies()))
                .append(MiraiCodes.WRAP)
                .append("需要研究点：").append(NovaEngUtils.formatDecimal(data.getRequiredPoints()))
                .append(MiraiCodes.WRAP)
                .append("最低算力要求：").append(NovaEngUtils.formatFLOPS(data.getMinComputationPointPerTick()))
                .append(MiraiCodes.WRAP)
                .build();
        MiscUtil.sendMessageToGroup(msg, event.getGroup());
        lastExecuted = System.currentTimeMillis();
    }

    private static MessageChain stringListToChain(List<String> strList) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (final String s : strList) {
            builder.append(s).append(MiraiCodes.WRAP);
        }
        return builder.build();
    }

    private static MessageChain dependenciesToChain(List<String> strList) {
        MessageChainBuilder builder = new MessageChainBuilder();
        if (strList.isEmpty()) {
            return builder.append("无").build();
        }
        for (final String s : strList) {
            builder.append(s).append("，");
        }
        return builder.build();
    }
}
