package github.kasuminova.kasuminovabot.module.serverhelper;

import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.network.message.whitelist.FullWhiteListInfo;
import github.kasuminova.network.message.whitelist.WhiteListInfo;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class GroupMessages {
    public static final String WHITELIST_ID_NOT_EXIST = "找不到此游戏 ID 对应的服务器白名单。";
    public static final String WHITELIST_QQ_NOT_EXIST = "找不到此 QQ 对应的服务器白名单。";

    public static String whiteListAddSucceeded(FullWhiteListInfo fullWhiteListInfo) {
        return String.format("已成功在服务器添加白名单: %s (QQ: %s)。",
                fullWhiteListInfo.getUserName(), fullWhiteListInfo.getId());
    }

    public static String whiteListRemoveSucceeded(FullWhiteListInfo fullWhiteListInfo) {
        return String.format("已成功将目标信息 %s (QQ: %s) 删除 / 移至回收站。",
                fullWhiteListInfo.getUserName(), fullWhiteListInfo.getId());
    }

    public static MessageChain whiteListUpdateSucceeded(FullWhiteListInfo fullWhiteListInfo) {
        return new MessageChainBuilder()
                .append("已成功更新用户白名单信息。").append(MiraiCodes.WRAP)
                .append("新用户信息: ").append(MiraiCodes.WRAP)
                .append("游戏 ID: ").append(fullWhiteListInfo.getUserName()).append(MiraiCodes.WRAP)
                .append("绑定 QQ: ").append(fullWhiteListInfo.getIdAsString()).append(MiraiCodes.WRAP)
                .append("最后一次更新时间: ")
                //将 long 类型转换为时间
                .append(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(fullWhiteListInfo.getLastUpdateTime()),
                        ZoneId.systemDefault()).toString()).append(MiraiCodes.WRAP)
                .build();
    }

    public static MessageChain whiteListInfo(FullWhiteListInfo fullWhiteListInfo) {
        MessageChainBuilder builder = new MessageChainBuilder();

        builder.append("目标信息: ").append(MiraiCodes.WRAP)
                .append("游戏 ID: ").append(fullWhiteListInfo.getUserName()).append(MiraiCodes.WRAP)
                .append("绑定 QQ: ").append(fullWhiteListInfo.getIdAsString()).append(MiraiCodes.WRAP)
                .append("最后一次更新时间: ")
                //将 long 类型转换为时间
                .append(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(fullWhiteListInfo.getLastUpdateTime()),
                        ZoneId.systemDefault()).toString()).append(MiraiCodes.WRAP)
                .append(MiraiCodes.WRAP)
                .append("白名单更新记录: ").append(MiraiCodes.WRAP);

        List<WhiteListInfo> history = fullWhiteListInfo.getHistory();
        if (history.isEmpty()) {
            builder.append("无历史记录").append(MiraiCodes.WRAP);
        } else {
            for (int i = 0; i < (Math.min(history.size(), 5)); i++) {
                builder.append(String.format("历史记录 (%s / %s)", i + 1, history.size())).append(MiraiCodes.WRAP)
                        .append("游戏 ID: ").append(history.get(i).getUserName()).append(MiraiCodes.WRAP)
                        .append("绑定 QQ: ").append(history.get(i).getIdAsString()).append(MiraiCodes.WRAP)
                        .append("更新时间: ")
                        //将 long 类型转换为时间
                        .append(LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(history.get(i).getLastUpdateTime()),
                                ZoneId.systemDefault()).toString()).append(MiraiCodes.WRAP)
                        .append(MiraiCodes.WRAP);

                if (i == 4) {
                    builder.append("历史记录超过 5 条，仅显示前 5 条信息。");
                    break;
                }
            }
        }

        return builder.build();
    }

    public static String whiteListUserNameAlreadyExists(FullWhiteListInfo fullWhiteListInfo) {
        return String.format("名为 %s 的游戏 ID 已经被 QQ %s 注册了！",
                fullWhiteListInfo.getUserName(),
                fullWhiteListInfo.getIdAsString());
    }

    public static String whiteListIDAlreadyExists(FullWhiteListInfo fullWhiteListInfo) {
        return String.format("你的 QQ 已经绑定了游戏 ID %s 了！", fullWhiteListInfo.getUserName());
    }
}
