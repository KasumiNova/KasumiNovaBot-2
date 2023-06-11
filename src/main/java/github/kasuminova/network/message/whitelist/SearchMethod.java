package github.kasuminova.network.message.whitelist;

public interface SearchMethod {
    int SEARCH_ID = 0;
    int SEARCH_QQ = 1;

    static int getIntWithString(String methodName) {
        switch (methodName) {
            case "ID": return SEARCH_ID;
            case "QQ": return SEARCH_QQ;
            default: return -1;
        }
    }
}
