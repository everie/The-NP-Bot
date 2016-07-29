package DataObjects;

/**
 * Created by Hans on 27-07-2016.
 */
public class UserScore implements Comparable<UserScore>
{
    private String nick;
    private String lastfm;
    private int score;

    public UserScore(String _nick, String _lastfm) {
        this.nick = _nick;
        this.lastfm = _lastfm;
    }

    public UserScore(String _nick, String _lastfm, int _score) {
        this.nick = _nick;
        this.lastfm = _lastfm;
        this.score = _score;
    }

    public String getNick() {
        return nick;
    }

    public String getLastFM() {
        return lastfm;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int _score) {
        this.score = _score;
    }

    public int compareTo(UserScore us) {
        return us.getScore() - this.score;
    }
}
