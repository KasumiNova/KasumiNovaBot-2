package github.kasuminova.network.message.serverinfo;

import java.io.Serializable;

public class OnlinePlayerListMessage implements Serializable {
    public int totalOnline;
    public String[] playerList;

    public OnlinePlayerListMessage(int totalOnline) {
        this.totalOnline = totalOnline;
        this.playerList = new String[0];
    }

    public OnlinePlayerListMessage(int totalOnline, String... playerList) {
        this.totalOnline = totalOnline;
        this.playerList = playerList;
    }
}
