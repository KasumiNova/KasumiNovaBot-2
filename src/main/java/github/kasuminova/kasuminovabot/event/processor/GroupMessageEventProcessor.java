package github.kasuminova.kasuminovabot.event.processor;

import github.kasuminova.kasuminovabot.event.listener.GroupMessageListener;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public abstract class GroupMessageEventProcessor {
    private final GroupMessageListener listener;
    private final EventProcessor<GroupMessageEvent> processor;

    public GroupMessageEventProcessor(GroupMessageListener listener) {
        this.listener = listener;
        this.processor = getMessageProcessor();
    }

    public abstract EventProcessor<GroupMessageEvent> getMessageProcessor();

    public void load() {
        listener.addEventProcessor(processor);
    }

    public void unLoad() {
        listener.removeEventProcessor(processor);
    }
}
