package github.kasuminova.network.message.whitelist;

public interface SearchMethod {
    int SEARCH_USERNAME = 0;
    int SEARCH_QQ = 1;

    static int getIntWithString(String methodName) {
        switch (methodName) {
            case "USERNAME": return SEARCH_USERNAME;
            case "QQ": return SEARCH_QQ;
            default: return -1;
        }
    }
}
