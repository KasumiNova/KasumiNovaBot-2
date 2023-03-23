package github.kasuminova.network.message.serverinfo;

import java.io.Serializable;

public class OnlineGetMessage implements Serializable {
    public boolean getPlayerList;

    public OnlineGetMessage(boolean getPlayerList) {
        this.getPlayerList = getPlayerList;
    }
}
