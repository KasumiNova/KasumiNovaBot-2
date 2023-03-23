package github.kasuminova.network.message.servercmd;

import java.io.Serializable;

public class CmdExecFailedMessage implements Serializable {
    public String serverName;
    public String sender;
    public String cause;

    public CmdExecFailedMessage(String serverName, String sender, String cause) {
        this.serverName = serverName;
        this.sender = sender;
        this.cause = cause;
    }
}
