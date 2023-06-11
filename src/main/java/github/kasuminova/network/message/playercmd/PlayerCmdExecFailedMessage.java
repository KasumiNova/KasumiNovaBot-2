package github.kasuminova.network.message.playercmd;

import java.io.Serializable;

public class PlayerCmdExecFailedMessage implements Serializable {
    public String serverName;
    public String playerName;
    public String sender;
    public String cause;

    public PlayerCmdExecFailedMessage(String playerName, String serverName, String sender, String cause) {
        this.serverName = serverName;
        this.playerName = playerName;
        this.sender = sender;
        this.cause = cause;
    }
}
