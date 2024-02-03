package github.kasuminova.kasuminovabot.module.serverhelper.event;

import github.kasuminova.kasuminovabot.event.processor.GroupMessageEventProcessor;
import github.kasuminova.kasuminovabot.event.processor.EventProcessor;
import github.kasuminova.kasuminovabot.event.listener.GroupMessageListener;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.network.message.chatmessage.GroupChatMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public class GroupMessageProcessor extends GroupMessageEventProcessor {
    private final ServerHelperCL cl;

    public GroupMessageProcessor(ServerHelperCL cl, GroupMessageListener eventListener) {
        super(eventListener);
        this.cl = cl;
    }

    @Override
    public EventProcessor<GroupMessageEvent> getMessageProcessor() {
        return event -> {
            if (event.getGroup().getId() == cl.getConfig().getGroupID()) {
                cl.sendMessageToServer(new GroupChatMessage(event.getSender().getId(), event.getMessage().contentToString()), false);
            }
            return false;
        };
    }
}
