package github.kasuminova.network.message.servercmd;

import java.io.Serializable;

public class CmdExecMessage implements Serializable {
    public String sender;
    public String serverName;
    public String cmd;

    public CmdExecMessage(String serverName, String sender, String cmd) {
        this.serverName = serverName;
        this.sender = sender;
        this.cmd = cmd;
    }
}
