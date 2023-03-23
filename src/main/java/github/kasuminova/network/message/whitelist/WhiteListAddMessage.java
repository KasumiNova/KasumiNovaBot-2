package github.kasuminova.network.message.whitelist;

import java.io.Serializable;

public class WhiteListAddMessage implements Serializable {
    public FullWhiteListInfo fullWhiteListInfo;

    public WhiteListAddMessage(FullWhiteListInfo fullWhiteListInfo) {
        this.fullWhiteListInfo = fullWhiteListInfo;
    }
}
