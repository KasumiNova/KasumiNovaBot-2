package github.kasuminova.kasuminovabot.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NetworkUtil {
    /**
     * <p>
     * 从指定链接下载文件并保存到指定文件中。
     * </p>
     *
     * <p>
     * 高度封装。
     * </p>
     *
     * @param url  链接
     * @param file 保存的文件名
     * @throws IOException 如果无法连接到服务器或 HTTP 状态码错误
     */
    public static void downloadFileWithURL(String url, File file) throws IOException, InterruptedException {
        try {
            downloadFileWithURL(url, file, -1);
        } catch (TimeoutException e) {
            throw new RuntimeException("Unexpected error", e);
        }
    }

    /**
     * <p>
     * 从指定链接下载文件并保存到指定文件中，支持设置超时时间。
     * </p>
     *
     * <p>
     * 高度封装。
     * </p>
     *
     * @param url        链接
     * @param file       保存的文件名
     * @param timeoutSec 超时时间（秒），-1 为始终等待
     * @throws TimeoutException 如果下载时间超过了超时时间
     * @throws IOException      如果无法连接到服务器或 HTTP 状态码错误
     */
    public static void downloadFileWithURL(String url, File file, int timeoutSec) throws TimeoutException, IOException, InterruptedException {
        FutureTask<Void> downloadTask = new FutureTask<>(() -> {
            if (file.exists()) {
                if (!file.delete()) {
                    throw new IOException("Could Not Delete File " + file.getPath());
                }
            }
            if (!file.createNewFile()) {
                throw new IOException("Could Not Create File " + file.getPath());
            }

            long downloaded = 0;

            InputStream fis = null;
            FileChannel foc = null;
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (!(responseCode >= 200 && responseCode <= 300)) {
                throw new IOException(String.format("无法连接至下载服务器或连接异常！(Code: %s)", responseCode));
            }

            try {
                fis = urlConnection.getInputStream();
                foc = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);

                ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

                int count;
                while ((count = fis.read(byteBuffer.array(), 0, 4096)) != -1 && !Thread.currentThread().isInterrupted()) {
                    foc.write(byteBuffer, downloaded);
                    downloaded += count;
                    byteBuffer.clear();
                }
            } finally {
                if (fis != null) {
                    fis.close();
                }
                if (foc != null) {
                    foc.close();
                }
            }

            return null;
        });

        Thread thread = new Thread(downloadTask);
        thread.start();

        try {
            if (timeoutSec == -1) {
                downloadTask.get();
            } else {
                downloadTask.get(timeoutSec, TimeUnit.SECONDS);
            }
        } catch (ExecutionException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
        } catch (TimeoutException e) {
            thread.interrupt();
            throw e;
        }
    }
}
