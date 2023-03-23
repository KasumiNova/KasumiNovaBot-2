package github.kasuminova.network.message.chatmessage;

public class GameChatMessage extends ChatMessage {
    public String userName;

    public GameChatMessage(String userName, String message) {
        super(message);
        this.userName = userName;
    }
}
