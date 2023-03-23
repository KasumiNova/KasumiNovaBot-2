package github.kasuminova.kasuminovabot.handler;

import github.kasuminova.kasuminovabot.handler.globallistener.GlobalGroupMessageListener;
import github.kasuminova.kasuminovabot.handler.globallistener.GroupMsgEventProcessor;

public abstract class AbstractSubListener {
    private final GlobalGroupMessageListener globalGroupMessageListener;
    private final GroupMsgEventProcessor processor;

    public AbstractSubListener(GlobalGroupMessageListener globalGroupMessageListener) {
        this.globalGroupMessageListener = globalGroupMessageListener;
        this.processor = getMessageProcessor();
    }

    public abstract GroupMsgEventProcessor getMessageProcessor();

    public void load() {
        globalGroupMessageListener.addGroupMessageSubProcessor(processor);
    }

    public void unLoad() {
        globalGroupMessageListener.removeGroupMessageSubProcessor(processor);
    }
}
