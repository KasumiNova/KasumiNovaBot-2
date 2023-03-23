package github.kasuminova.kasuminovabot.util;

import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;

public class MiraiCodes {
    public static final MessageChain WRAP = MiraiCode.deserializeMiraiCode("\n");

    public static MessageChain atTo(long id) {
        return MiraiCode.deserializeMiraiCode(String.format("[mirai:at:%s]", id));
    }
}
