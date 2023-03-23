package github.kasuminova.network.message.whitelist;

public interface ResultCode {
    int SUCCESS                 = 100;
    int USERNAME_ALREADY_EXISTS = 101;
    int ID_ALREADY_EXISTS       = 102;
    int USERNAME_NOT_EXIST      = 103;
    int ID_NOT_EXIST            = 104;
}
