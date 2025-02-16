package github.kasuminova.kasuminovabot.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class FileUtil {

    public static String readStringFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static String readStringFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void writeStringToFile(RandomAccessFile file, String str) throws IOException {
        file.setLength(0);
        file.write(str.getBytes(StandardCharsets.UTF_8));
    }

}
