package github.kasuminova.kasuminovabot.util;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Random;

public class MiscUtil {
    private static final Random RANDOM = new Random();

    /**
     * 根据 QQ，获得一个指定范围内的随机数。但是同一个种子每天都是固定的数字，第二天就会变化。
     *
     * @param id    QQ 号
     * @param range 范围
     * @return 范围内的随机数
     */
    public static int getRandomIntWithQQ(Long id, int range) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        long randomNum = Long.parseLong(LocalDate.now().format(formatter)) * id;

        //以 randomNum 为随机数种子生成随机数
        RANDOM.setSeed(randomNum);

        //返回指定范围的随机数
        return RANDOM.nextInt(range);
    }

    /**
     * 根据种子，获得一个指定范围内的随机数。但是同一个种子每天都是固定的数字，第二天就会变化。
     *
     * @param seed  种子
     * @param range 范围
     * @return 范围内的随机数
     */
    public static int getRandomIntWithSeedAndDate(long seed, int range) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        long randomNum = Long.parseLong(LocalDate.now().format(formatter));

        //以 randomNum 和 seed 为随机数种子生成随机数
        RANDOM.setSeed(randomNum + seed);

        //返回指定范围的随机数
        return RANDOM.nextInt(range);
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str 字符串
     * @return 转换后的 byte 数组
     */
    public static byte[] toByteArr(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    /**
     * 数组头是否与目标相同
     *
     * @param array 要检查的数组
     * @param head  数组头
     * @return 相同返回 true，反之返回 false
     */
    public static boolean arrayHeadEquals(byte[] array, byte[] head) {
        if (head.length < array.length) return false;

        for (int i = 0; i < head.length; i++) {
            if (array[i] != head[i]) return false;
        }

        return true;
    }

    public static void sendMessageToGroup(MessageChain message, long botId, long groupId) {
        Bot bot;

        try {
            bot = Bot.getInstance(botId);
            Group group = bot.getGroup(groupId);
            if (group == null) {
                KasumiNovaBot2.INSTANCE.logger.warning(String.format("Cloud not find group %s!", groupId));
                return;
            }

            if (group.getBotAsMember().isMuted() || group.getSettings().isMuteAll()) {
                return;
            }

            group.sendMessage(message);
        } catch (NoSuchElementException e) {
            KasumiNovaBot2.INSTANCE.logger.warning(String.format("Cloud not find bot %s!", botId));
        }
    }

    public static void sendMessageToGroup(MessageChain message, Group group) {
        NormalMember bot = group.getBotAsMember();
        if (bot.isMuted() || (group.getSettings().isMuteAll() && bot.getPermission().getLevel() < 1)) {
            return;
        }

        group.sendMessage(message);
    }

    /**
     * 字符串是否都为数字
     *
     * @param str 字符串
     * @return 当全为数字时返回 true，否则返回 false
     */
    public static boolean isNum(String str) {
        return str.matches("-?[0-9]+");
    }

    /**
     * 字符串是否为有效用户名
     *
     * @param userName 用户名
     * @return ID 只能由字母、数字和下划线组成，且长度至少为 3 个字符，最多为 16 个字符，符合以上条件返回 {@code true} 否则返回 {@code false}
     */
    public static boolean isValidUserName(String userName) {
        return !isNum(userName) && userName.length() >= 3 && userName.length() <= 16 && userName.matches("^[0-9a-zA-Z_]+$");
    }
}
