package github.kasuminova.network.message.chatmessage;

public class GroupChatMessage extends ChatMessage {
    public long id;

    public GroupChatMessage(long qq, String message) {
        super(message);
        this.id = qq;
    }
}
