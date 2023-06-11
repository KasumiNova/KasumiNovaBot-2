package github.kasuminova.network.message.playercmd;

import java.io.Serializable;

public class KickMeMessage implements Serializable {
    public String id;

    public KickMeMessage(String id) {
        this.id = id;
    }
}
