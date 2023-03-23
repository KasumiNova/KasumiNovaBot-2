package github.kasuminova.kasuminovabot.handler.globallistener;

import net.mamoe.mirai.event.events.GroupMessageEvent;

public interface GroupMsgEventProcessor {
    boolean onMessage(GroupMessageEvent event);
}
