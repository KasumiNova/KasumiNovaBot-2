package github.kasuminova.kasuminovabot.module.serverhelper.group.handler;

import github.kasuminova.kasuminovabot.handler.AbstractSubListener;
import github.kasuminova.kasuminovabot.handler.globallistener.GlobalGroupMessageListener;
import github.kasuminova.kasuminovabot.handler.globallistener.GroupMsgEventProcessor;
import github.kasuminova.kasuminovabot.module.serverhelper.ServerHelperCL;
import github.kasuminova.network.message.chatmessage.GroupChatMessage;

public class GroupMessageListener extends AbstractSubListener {
    private final ServerHelperCL cl;

    public GroupMessageListener(ServerHelperCL cl, GlobalGroupMessageListener globalGroupMessageListener) {
        super(globalGroupMessageListener);
        this.cl = cl;
    }

    @Override
    public GroupMsgEventProcessor getMessageProcessor() {
        return event -> {
            if (event.getGroup().getId() == cl.getConfig().getGroupID()) {
                cl.sendMessageToServer(new GroupChatMessage(event.getSender().getId(), event.getMessage().contentToString()), false);
            }
            return false;
        };
    }
}
