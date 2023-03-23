package github.kasuminova.network.message.chatmessage;

import java.io.Serializable;

public abstract class ChatMessage implements Serializable {
    public String message;

    public ChatMessage(String message) {
        this.message = message;
    }
}
