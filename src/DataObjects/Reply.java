package DataObjects;

/**
 * Created by Hans on 26-07-2016.
 */
public class Reply {
    String message;
    String type;
    String channel;
    String moreMessage;

    public Reply() {
        // Empty Constructor
    }

    public Reply(String type, String message, String channel) {
        this.type = type;
        this.message = message;
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public String getMoreMessage() {
        return moreMessage;
    }

    public String getType() {
        return type;
    }

    public String getChannel() {
        return channel;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setMoreMessage(String moreMessage) {
        this.moreMessage = moreMessage;
    }
}
