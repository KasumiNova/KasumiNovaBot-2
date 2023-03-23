package github.kasuminova.network.message.whitelist;

import java.io.Serializable;
import java.util.ArrayList;

public class WhiteListInfo implements Serializable {
    private final String userName;
    private final long id;
    private long lastUpdateTime;

    public WhiteListInfo(String userName, long id, long lastUpdateTime) {
        this.userName = userName;
        this.id = id;
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getId() {
        return id;
    }

    public String getIdAsString() {
        return String.valueOf(id);
    }

    public WhiteListInfo setLastUpdateTimeNow() {
        this.lastUpdateTime = System.currentTimeMillis();
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public FullWhiteListInfo toFullWhiteListInfo() {
        return new FullWhiteListInfo(userName, id, lastUpdateTime, new ArrayList<>(0));
    }
}
