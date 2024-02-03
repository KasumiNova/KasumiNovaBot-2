package github.kasuminova.kasuminovabot.module.entertainment;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import github.kasuminova.kasuminovabot.module.tips.TipManager;
import github.kasuminova.kasuminovabot.util.FileUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class TipManagerSyncThread implements Runnable {
    private static final AtomicInteger THREAD_COUNT = new AtomicInteger(0);

    private final RandomAccessFile file;
    private Thread thread = null;

    public TipManagerSyncThread(RandomAccessFile file) {
        this.file = file;
    }

    public void start() {
        thread = new Thread(this);
        thread.setName("ChatMessageSyncTask-" + THREAD_COUNT.getAndIncrement());
        thread.start();
    }

    public void interrupt() {
        if (thread != null) thread.interrupt();
    }

    @Override
    public void run() {
        KasumiNovaBot2.INSTANCE.logger.info("随机提示文件同步线程已启动。");

        while (!Thread.currentThread().isInterrupted()) {
            if (TipManager.getChanged()) {
                String encoded = TipManager.encodeToJsonString();
                try {
                    FileUtil.writeStringToFile(file, encoded);
                } catch (IOException e) {
                    KasumiNovaBot2.INSTANCE.logger.warning("写入随机提示文件数据失败！", e);
                }
            }
            LockSupport.parkNanos((long) (1F * 1000 * 1000 * 1000));
        }

        KasumiNovaBot2.INSTANCE.logger.info("随机提示文件同步线程已终止。");
    }

}
