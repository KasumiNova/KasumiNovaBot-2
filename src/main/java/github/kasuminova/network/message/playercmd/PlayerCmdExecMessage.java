package github.kasuminova.network.message.playercmd;

import java.io.Serializable;

public class PlayerCmdExecMessage implements Serializable {
    public String playerName;
    public String serverName;
    public String sender;
    public String cmd;

    public PlayerCmdExecMessage(String playerName, String serverName, String sender, String cmd) {
        this.serverName = serverName;
        this.playerName = playerName;
        this.sender = sender;
        this.cmd = cmd;
    }
}
