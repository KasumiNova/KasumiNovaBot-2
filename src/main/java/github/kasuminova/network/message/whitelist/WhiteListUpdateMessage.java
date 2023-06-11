package github.kasuminova.network.message.whitelist;

import java.io.Serializable;

public class WhiteListUpdateMessage implements Serializable, SearchMethod {
    public int searchMethod;
    public String oldName;
    public String newName;
    public long oldId;
    public long newId;

    public WhiteListUpdateMessage(long oldId, long newId) {
        this.searchMethod = SEARCH_QQ;
        this.oldId = oldId;
        this.newId = newId;
        this.oldName = "";
        this.newName = "";
    }

    public WhiteListUpdateMessage(String oldName, String newName) {
        this.searchMethod = SEARCH_ID;
        this.oldId = -1;
        this.newId = -1;
        this.oldName = oldName;
        this.newName = newName;
    }
}
