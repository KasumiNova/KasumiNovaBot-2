package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.module.entertainment.config.hitokoto.HitokotoAPI;
import github.kasuminova.kasuminovabot.module.tips.CustomTip;
import github.kasuminova.kasuminovabot.module.tips.TipManager;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.kasuminovabot.util.MiscUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class RandomHitokotoCmd extends GroupCommand {
    public static final RandomHitokotoCmd INSTANCE = new RandomHitokotoCmd();

    private long lastExecuted = 0;

    private RandomHitokotoCmd() {
        super("随机一言",
                0,
                0,
                0,
                new MessageChainBuilder()
                        .append("介绍：获取随机一言，从一言接口获取（冷却时间 10 秒），有 50% 概率获取各路群友提交的随机提示。")
                        .build()
        );
    }

    @Override
    public void execute(final GroupMessageEvent event, final List<String> args) {
        long current = System.currentTimeMillis();
        if (lastExecuted + (10 * 1000) > current) {
            return;
        }

        CompletableFuture.runAsync(
                ThreadLocalRandom.current().nextFloat() >= 0.5
                ? (() -> sendRandomTip(event))
                : (() -> sendHitokoto(event)));

        lastExecuted = System.currentTimeMillis();
    }

    private static void sendRandomTip(final GroupMessageEvent event) {
        CustomTip randomTip = TipManager.randomTip();
        if (randomTip == null) {
            sendHitokoto(event);
            return;
        }

        String who;
        NormalMember groupMember = event.getGroup().getMembers().get(randomTip.getSubmitter());
        if (groupMember != null) {
            who = groupMember.getNick();
        } else {
            who = randomTip.getWho();
        }

        MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append(randomTip.getTip()).append(" —— ").append(who)
                .append("(ID: ").append(String.valueOf(randomTip.getId())).append(" )")
                .build(), event.getGroup());
    }

    private static void sendHitokoto(final GroupMessageEvent event) {
        Group group = event.getGroup();
        String hitokoto = HitokotoAPI.getRandomHitokoto();
        if (hitokoto.isEmpty()) {
            MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("获取失败，请稍后重试。")
                    .build(), group);
        }

        MiscUtil.sendMessageToGroup(new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append(hitokoto)
                .append("(Hitokoto API)")
                .build(), group);
    }
}
