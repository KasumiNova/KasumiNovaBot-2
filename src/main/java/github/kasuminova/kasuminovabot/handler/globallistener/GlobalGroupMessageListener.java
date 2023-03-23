package github.kasuminova.kasuminovabot.handler.globallistener;

import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.LinkedList;
import java.util.List;

public class GlobalGroupMessageListener {
    private final List<GroupMsgEventProcessor> listeners = new LinkedList<>();
    private Listener<GroupMessageEvent> groupListener;

    public void subscribe() {
        if (groupListener != null) {
            unSubscribe();
        }

        groupListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            for (GroupMsgEventProcessor listener : listeners) {
                if (listener.onMessage(event)) {
                    break;
                }
            }
        });
    }

    public void unSubscribe() {
        if (groupListener.isActive()) {
            groupListener.complete();
            groupListener = null;
        }
    }

    public void addGroupMessageSubProcessor(GroupMsgEventProcessor listener) {
        listeners.add(listener);
    }

    public void removeGroupMessageSubProcessor(GroupMsgEventProcessor listener) {
        listeners.remove(listener);
    }
}
