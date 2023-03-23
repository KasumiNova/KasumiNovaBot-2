package github.kasuminova.network.message.whitelist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FullWhiteListInfo implements Serializable {
    private final List<WhiteListInfo> history;
    private String userName;
    private long id;
    private long lastUpdateTime;

    public FullWhiteListInfo(String userName, long id, long lastUpdateTime, List<WhiteListInfo> history) {
        this.userName = userName;
        this.id = id;
        this.lastUpdateTime = lastUpdateTime;
        this.history = history;
    }

    public long getId() {
        return id;
    }

    public FullWhiteListInfo setId(long id) {
        this.id = id;
        return this;
    }

    public String getIdAsString() {
        return String.valueOf(id);
    }

    public void setLastUpdateTimeNow() {
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public String getUserName() {
        return userName;
    }

    public FullWhiteListInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * 添加白名单历史记录
     *
     * @param fullWhiteListInfo 新记录
     * @param mergeHistory      合并新记录的历史记录
     */
    public void addHistory(FullWhiteListInfo fullWhiteListInfo, boolean mergeHistory) {
        this.history.add(fullWhiteListInfo.toWhiteListInfo());
        if (mergeHistory) {
            mergeHistory(fullWhiteListInfo);
        }
    }

    public List<WhiteListInfo> getHistory() {
        return history;
    }

    /**
     * 合并白名单信息的历史记录
     *
     * @param fullWhiteListInfo 要合并的白名单信息
     */
    public void mergeHistory(FullWhiteListInfo fullWhiteListInfo) {
        this.history.addAll(fullWhiteListInfo.history);
    }

    public WhiteListInfo toWhiteListInfo() {
        return new WhiteListInfo(userName, id, lastUpdateTime);
    }

    public final FullWhiteListInfo copySelf(boolean addSelfToHistory) {
        ArrayList<WhiteListInfo> newHistory = new ArrayList<>(history);
        if (addSelfToHistory) {
            addHistory(this, false);
        }
        return new FullWhiteListInfo(userName, id, lastUpdateTime, newHistory);
    }
}
