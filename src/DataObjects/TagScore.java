package DataObjects;

/**
 * Created by Hans on 29-07-2016.
 */
public class TagScore implements Comparable<TagScore>
{
    private String tag;
    private int score;

    public TagScore(String _tag, int _score) {
        this.tag = _tag;
        this.score = _score;
    }

    public String getTag() {
        return tag;
    }

    public int getScore() {
        return score;
    }

    public void incScore(int _score) {
        this.score += _score;
    }

    public int compareTo(TagScore ts) {
        return ts.getScore() - this.score;
    }
}