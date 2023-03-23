package github.kasuminova.network.message.protocol;

import java.io.Serializable;

public class PreDisconnectMessage implements Serializable {
    public String reason;

    public PreDisconnectMessage(String reason) {
        this.reason = reason;
    }
}
