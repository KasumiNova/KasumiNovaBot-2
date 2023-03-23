package github.kasuminova.network.message.protocol;

import java.io.Serializable;

public class ClientTypeMessage implements Serializable {
    public String type;
    public String version;
    public String accessToken;
    public String clientId;

    public ClientTypeMessage(String type, String version, String accessToken, String clientId) {
        this.type = type;
        this.version = version;
        this.accessToken = accessToken;
        this.clientId = clientId;
    }
}
