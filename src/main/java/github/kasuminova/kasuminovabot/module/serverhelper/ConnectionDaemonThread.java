package github.kasuminova.kasuminovabot.module.serverhelper;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class ConnectionDaemonThread implements Runnable {
    private static final AtomicInteger THREAD_COUNT = new AtomicInteger(0);
    private final ServerHelperCL cl;
    private Thread thread;

    public ConnectionDaemonThread(ServerHelperCL cl) {
        this.cl = cl;
    }

    public void start() {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(this);
            thread.setName("ConnectionDaemonThread-" + THREAD_COUNT.getAndIncrement());
            thread.start();
        }
    }

    public void interrupt() {
        if (!thread.isInterrupted()) thread.interrupt();
    }

    @Override
    public void run() {
        KasumiNovaBot2.INSTANCE.logger.info("连接守护线程已启动.");

        logic:
        while (!Thread.currentThread().isInterrupted()) {
            if (cl.future != null) {
                LockSupport.parkNanos(1000L * 1000 * 1000);
                continue;
            }

            KasumiNovaBot2.INSTANCE.logger.warning("已失去对中心服务器的连接！正在尝试重连...");
            boolean isConnected = false;
            int retryCount = 0;
            do {
                try {
                    cl.connect();
                    isConnected = true;
                    break;
                } catch (Exception e) {
                    KasumiNovaBot2.INSTANCE.logger.warning("重连错误: " + e);
                    KasumiNovaBot2.INSTANCE.logger.info("等待 5 秒后重试...");

                    LockSupport.parkNanos(5L * 1000 * 1000 * 1000);
                    if (Thread.currentThread().isInterrupted()) {
                        break logic;
                    }
                }
                retryCount++;
            } while (retryCount <= 5);

            if (!isConnected) {
                KasumiNovaBot2.INSTANCE.logger.warning("重试已超过 5 次，等待 30 秒后重新连接.");
                LockSupport.parkNanos(30L * 1000 * 1000 * 1000);
            }
        }

        KasumiNovaBot2.INSTANCE.logger.info("连接守护线程已终止.");
        THREAD_COUNT.getAndDecrement();
    }
}
