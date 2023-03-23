package github.kasuminova.network.message;

public interface MessageProcessor<HANDLER, MESSAGE> {
    void process(HANDLER handler, MESSAGE message);
}
