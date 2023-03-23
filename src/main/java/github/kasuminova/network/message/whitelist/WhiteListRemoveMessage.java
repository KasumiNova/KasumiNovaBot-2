package github.kasuminova.network.message.whitelist;

import java.io.Serializable;

public class WhiteListRemoveMessage implements Serializable, SearchMethod {
    public int searchMethod;
    public String key;
    public boolean removeRecycleBin;

    public WhiteListRemoveMessage(long qq, boolean removeRecycleBin) {
        this.searchMethod = SEARCH_QQ;
        this.key = String.valueOf(qq);
        this.removeRecycleBin = removeRecycleBin;
    }

    public WhiteListRemoveMessage(String userName, boolean removeRecycleBin) {
        this.searchMethod = SEARCH_USERNAME;
        this.key = userName;
        this.removeRecycleBin = removeRecycleBin;
    }
}
