package github.kasuminova.network.message.whitelist;

import java.io.Serializable;

public class WhiteListGetMessage implements Serializable, SearchMethod {
    public int searchMethod;
    public String key;

    public WhiteListGetMessage(long qq) {
        this.searchMethod = SEARCH_QQ;
        this.key = String.valueOf(qq);
    }

    public WhiteListGetMessage(String userName) {
        this.searchMethod = SEARCH_ID;
        this.key = userName;
    }
}
