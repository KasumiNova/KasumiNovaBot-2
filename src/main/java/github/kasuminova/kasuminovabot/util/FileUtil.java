package github.kasuminova.kasuminovabot.util;

import github.kasuminova.kasuminovabot.module.tips.TipManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class FileUtil {
    public static String readStringFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public static String readStringFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public static void writeStringToFile(RandomAccessFile file, String str) throws IOException {
        file.setLength(0);
        file.write(str.getBytes(StandardCharsets.UTF_8));
    }

}
