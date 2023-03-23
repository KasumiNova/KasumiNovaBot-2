package github.kasuminova.network.message.whitelist;

import java.io.Serializable;

public class WhiteListUpdateResult implements Serializable, UpdateType, ResultCode {
    int updateType;
    int resultCode;
    FullWhiteListInfo fullWhiteListInfo;

    public WhiteListUpdateResult(int updateType, FullWhiteListInfo fullWhiteListInfo) {
        this.updateType = updateType;
        this.resultCode = SUCCESS;
        this.fullWhiteListInfo = fullWhiteListInfo;
    }

    public WhiteListUpdateResult(int updateType, int resultCode) {
        this.updateType = updateType;
        this.resultCode = resultCode;
        this.fullWhiteListInfo = null;
    }

    public WhiteListUpdateResult(int updateType, int resultCode, FullWhiteListInfo fullWhiteListInfo) {
        this.updateType = updateType;
        this.resultCode = resultCode;
        this.fullWhiteListInfo = fullWhiteListInfo;
    }

    public int getUpdateType() {
        return updateType;
    }

    public int getResultCode() {
        return resultCode;
    }

    public FullWhiteListInfo getFullWhiteListInfo() {
        return fullWhiteListInfo;
    }

    public boolean isSuccess() {
        return resultCode == SUCCESS;
    }
}
