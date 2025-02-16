package github.kasuminova.kasuminovabot.module.mclogs;

import github.kasuminova.kasuminovabot.event.listener.GroupMessageListener;
import github.kasuminova.kasuminovabot.event.processor.EventProcessor;
import github.kasuminova.kasuminovabot.event.processor.GroupMessageEventProcessor;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.file.AbsoluteFile;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.FileMessage;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;

public class AutoUploader extends GroupMessageEventProcessor {

    public AutoUploader(final GroupMessageListener listener) {
        super(listener);
    }

    @Override
    public EventProcessor<GroupMessageEvent> getMessageProcessor() {
        return event -> {
            Group group = event.getGroup();
            MessageChain message = event.getMessage();
            for (final SingleMessage singleMessage : message) {
                if (singleMessage instanceof FileMessage fileMessage) {
                    AbsoluteFile absoluteFile = fileMessage.toAbsoluteFile(group);
                }
            }
            return false;
        };
    }

}
