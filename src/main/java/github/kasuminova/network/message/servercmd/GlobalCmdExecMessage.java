package github.kasuminova.network.message.servercmd;

import java.io.Serializable;

public class GlobalCmdExecMessage implements Serializable {
    public String sender;
    public String cmd;

    public GlobalCmdExecMessage(String sender, String cmd) {
        this.sender = sender;
        this.cmd = cmd;
    }
}
