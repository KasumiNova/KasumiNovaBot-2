package github.kasuminova.kasuminovabot.event.listener;

import net.mamoe.mirai.event.events.GroupMessageEvent;

public class GroupMessageListener extends EventListener<GroupMessageEvent> {
    public GroupMessageListener() {
        super(GroupMessageEvent.class);
    }
}
