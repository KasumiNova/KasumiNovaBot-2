package github.kasuminova.network.message.whitelist;

public class UserInGroupResultMessage {

    public long id;
    public boolean exists;

    public UserInGroupResultMessage(final long id, final boolean exists) {
        this.id = id;
        this.exists = exists;
    }

    public UserInGroupResultMessage() {
    }

}
