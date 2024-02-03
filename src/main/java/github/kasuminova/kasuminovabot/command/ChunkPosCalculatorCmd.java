package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;

public class ChunkPosCalculatorCmd extends GroupCommand {
    public static final ChunkPosCalculatorCmd INSTANCE = new ChunkPosCalculatorCmd();

    private ChunkPosCalculatorCmd() {
        super("区块计算",
                2,
                2,
                0,
                new MessageChainBuilder()
                        .append("介绍：计算 MC 区块坐标。").append(MiraiCodes.WRAP)
                        .append(String.format("例：%s区块计算 30443 44887", KasumiNovaBot2.COMMAND_PREFIX))
                        .build()
        );
    }

    @Override
    public void execute(final GroupMessageEvent event, final List<String> args) {
        String xStr = args.get(0);
        String zStr = args.get(1);
        if (!MiscUtil.isNum(xStr)) {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("非法参数 ").append(xStr).append(MiraiCodes.WRAP)
                    .append("应为纯数字。")
                    .build(), event.getGroup());
            return;
        }
        if (!MiscUtil.isNum(zStr)) {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("非法参数 ").append(zStr).append(MiraiCodes.WRAP)
                    .append("应为纯数字。")
                    .build(), event.getGroup());
            return;
        }

        int x = Integer.parseInt(xStr);
        int z = Integer.parseInt(zStr);

        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        int relativeChunkX = chunkX & 31;
        int relativeChunkZ = chunkZ & 31;
        int regionX = chunkX >> 5;
        int regionZ = chunkZ >> 5;

        MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append("计算结果：").append(MiraiCodes.WRAP)
                .append("区块 X 坐标：").append(String.valueOf(chunkX)).append(MiraiCodes.WRAP)
                .append("区块 Z 坐标：").append(String.valueOf(chunkZ)).append(MiraiCodes.WRAP)
                .append("相对区块 X 坐标：").append(String.valueOf(relativeChunkX)).append(MiraiCodes.WRAP)
                .append("相对区块 Z 坐标：").append(String.valueOf(relativeChunkZ)).append(MiraiCodes.WRAP)
                .append("区域文件 X 坐标：").append(String.valueOf(regionX)).append(MiraiCodes.WRAP)
                .append("区域文件 Z 坐标：").append(String.valueOf(regionZ)).append(MiraiCodes.WRAP)
                .append("对应区域文件：").append(String.format("r.%s.%s.mca", regionX, regionZ))
                .build(), event.getGroup());
    }
}
