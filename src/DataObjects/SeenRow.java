package DataObjects;

/**
 * Created by Hans on 29-07-2016.
 */
public class SeenRow {
    private String nick;
    private long seen;
    private String comment;

    public SeenRow (String _nick, long _seen, String _comment) {
        this.nick = _nick;
        this.seen = _seen;
        this.comment = _comment;
    }

    public String getNick() {
        return nick;
    }

    public long getSeen() {
        return seen;
    }

    public String getComment() {
        return comment;
    }
}
