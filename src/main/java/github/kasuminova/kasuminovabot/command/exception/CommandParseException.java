package github.kasuminova.kasuminovabot.command.exception;

import net.mamoe.mirai.message.data.MessageChain;

public class CommandParseException extends Exception {

    private final MessageChain errorMessage;

    public CommandParseException(MessageChain errorMessage) {
        this.errorMessage = errorMessage;
    }

    public MessageChain getErrorMessage() {
        return errorMessage;
    }
}
