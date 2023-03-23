package github.kasuminova.kasuminovabot.module.serverhelper;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.serverhelper.config.ServerHelperCLConfig;
import github.kasuminova.kasuminovabot.util.MiraiCodes;
import github.kasuminova.network.message.chatmessage.GameChatMessage;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class ServerMessageSyncThread implements Runnable {
    private static final AtomicInteger THREAD_COUNT = new AtomicInteger(0);
    private static final MessageChain MESSAGE_TOO_LONG = new MessageChainBuilder().append("指令执行消息过长，已忽略结果。").build();
    public final LinkedBlockingQueue<GameChatMessage> messageQueue = new LinkedBlockingQueue<>();
    public final Map<String, LinkedBlockingQueue<String>> cmdExecResults = new TreeMap<>();
    private final ServerHelperCL cl;
    private int start;
    private int end;
    private Thread thread = null;

    public ServerMessageSyncThread(ServerHelperCL cl) {
        this.cl = cl;
    }

    public void load() {
        ServerHelperCLConfig.CLConfig config = cl.getConfig();

        try {
            start = new SimpleDateTime(config.getChatSyncStartTime()).toInt();
            end = new SimpleDateTime(config.getChatSyncEndTime()).toInt();
        } catch (IllegalArgumentException e) {
            KasumiNovaBot2.INSTANCE.logger.error(e);
            KasumiNovaBot2.INSTANCE.logger.error("配置文件中的消息同步时间参数有误！使用默认时间...");
            start = new SimpleDateTime("00:00:00").toInt();
            end = new SimpleDateTime("23:59:59").toInt();
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.setName("ChatMessageSyncTask-" + THREAD_COUNT.getAndIncrement());
        thread.start();
    }

    public void interrupt() {
        if (thread != null) thread.interrupt();
    }

    public void offerCmdExecResult(String serverName, List<String> results) {
        LinkedBlockingQueue<String> queue = cmdExecResults.computeIfAbsent(serverName, q -> new LinkedBlockingQueue<>());
        for (String result : results) {
            queue.offer(result);
        }
    }

    @Override
    public void run() {
        KasumiNovaBot2.INSTANCE.logger.info("消息同步线程已启动.");

        while (!Thread.currentThread().isInterrupted()) {
            MessageChain message = buildGameChatMessageChain(messageQueue);
            if (!message.isEmpty() && message.size() <= 20) {
                cl.sendMessage(message);
            }

            message = buildCmdExecResultMessageChain(cmdExecResults);
            if (!message.isEmpty()) {
                if (message.size() <= 20) {
                    cl.sendMessage(message);
                } else {
                    cl.sendMessage(MESSAGE_TOO_LONG);
                }
            }

            LockSupport.parkNanos((long) (7.5F * 1000 * 1000 * 1000));
        }

        KasumiNovaBot2.INSTANCE.logger.info("消息同步线程已停止.");
    }

    private static MessageChain buildGameChatMessageChain(Queue<GameChatMessage> messageQueue) {
        MessageChainBuilder builder = new MessageChainBuilder();

        GameChatMessage message;
        while ((message = messageQueue.poll()) != null) {
            builder.append(message.userName).append(": ").append(message.message);

            if (messageQueue.isEmpty()) {
                break;
            } else {
                builder.append(MiraiCodes.WRAP);
            }
        }

        return builder.build();
    }

    private static MessageChain buildCmdExecResultMessageChain(Map<String, LinkedBlockingQueue<String>> messageQueue) {
        MessageChainBuilder builder = new MessageChainBuilder();

        if (!messageQueue.isEmpty()) {
            messageQueue.forEach((server, queue) -> {
                if (queue.isEmpty()) {
                    return;
                }

                builder.append('[').append(server).append("]:").append(MiraiCodes.WRAP);
                String message;
                while ((message = queue.poll()) != null) {
                    builder.append(message);

                    if (queue.isEmpty()) {
                        break;
                    } else {
                        builder.append(MiraiCodes.WRAP);
                    }
                }
            });
        }

        return builder.build();
    }

    public boolean canSendChatMessage() {
        int now = SimpleDateTime.localDateTimeToInt(LocalDateTime.now());

        return now > start && now < end;
    }

    public static class SimpleDateTime {
        public final short hour;
        public final short minute;
        public final short second;

        public SimpleDateTime(String time) {
            String[] split = time.split(":");
            if (split.length != 3) {
                throw new IllegalArgumentException("Invalid time " + time + " , valid is HH:mm:ss");
            }

            hour = Short.parseShort(split[0]);
            minute = Short.parseShort(split[1]);
            second = Short.parseShort(split[2]);
        }

        public static int localDateTimeToInt(LocalDateTime localDateTime) {
            return localDateTime.getHour() * 3600 + localDateTime.getMinute() * 60 + localDateTime.getSecond();
        }

        public int toInt() {
            return hour * 3600 + minute * 60 + second;
        }
    }
}
