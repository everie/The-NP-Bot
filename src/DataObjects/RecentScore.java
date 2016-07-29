package DataObjects;

/**
 * Created by Hans on 28-07-2016.
 */
public class RecentScore implements Comparable<RecentScore>
{
    private String artist;
    private String track;
    private int score = 1;

    public RecentScore(String _artist, String _track) {
        this.artist = _artist;
        this.track = _track;
    }

    public String getTrack() {
        return track;
    }

    public String getArtist() {
        return artist;
    }

    public int getScore() {
        return score;
    }

    public void incScore() {
        this.score++;
    }

    public int compareTo(RecentScore rs) {
        return rs.getScore() - this.score;
    }

    public String toString() {
        return track + ": " + String.valueOf(score);
    }
}