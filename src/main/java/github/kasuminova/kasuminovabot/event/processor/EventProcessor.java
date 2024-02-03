package github.kasuminova.kasuminovabot.event.processor;

import net.mamoe.mirai.event.Event;

public interface EventProcessor<E extends Event> {
    boolean onEvent(E event);
}
