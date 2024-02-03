package github.kasuminova.kasuminovabot.event.listener;

import github.kasuminova.kasuminovabot.event.processor.EventProcessor;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;

import java.util.LinkedList;
import java.util.List;

public abstract class EventListener<E extends Event> {
    private final Class<E> eventType;
    private final List<EventProcessor<E>> listeners = new LinkedList<>();
    private Listener<E> groupListener;

    protected EventListener(final Class<E> eventType) {
        this.eventType = eventType;
    }

    public void subscribe() {
        if (groupListener != null) {
            unSubscribe();
        }

        groupListener = GlobalEventChannel.INSTANCE.subscribeAlways(eventType, event -> {
            for (EventProcessor<E> listener : listeners) {
                if (listener.onEvent(event)) {
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

    public void addEventProcessor(EventProcessor<E> listener) {
        listeners.add(listener);
    }

    public void removeEventProcessor(EventProcessor<E> listener) {
        listeners.remove(listener);
    }
}
