package DataObjects;

/**
 * Created by Hans on 26-07-2016.
 */
public class Reply {
    String message;
    String type;
    String channel;

    public Reply(String type, String message, String channel) {
        this.type = type;
        this.message = message;
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getChannel() {
        return channel;
    }
}
