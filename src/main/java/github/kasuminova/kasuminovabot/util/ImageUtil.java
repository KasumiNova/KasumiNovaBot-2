package github.kasuminova.kasuminovabot.util;

public class ImageUtil {
    public static final byte[] JPG = MiscUtil.toByteArr("FFD8FF");
    public static final byte[] PNG = MiscUtil.toByteArr("89504E47");
    public static final byte[] GIF = MiscUtil.toByteArr("47494638");

    public static String checkImageType(byte[] head) {
        if (head.length < 4) return null;

        return MiscUtil.arrayHeadEquals(head, JPG) ? "jpg"
                : MiscUtil.arrayHeadEquals(head, PNG) ? "png"
                : MiscUtil.arrayHeadEquals(head, GIF) ? "gif"
                : null;
    }

}
