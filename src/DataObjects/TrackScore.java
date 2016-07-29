package DataObjects;

/**
 * Created by Hans on 27-07-2016.
 */
public class TrackScore implements Comparable<TrackScore>
{
    private String track;
    private int score = 1;

    public TrackScore(String _track) {
        this.track = _track;
    }

    public String getTrack() {
        return track;
    }

    public int getScore() {
        return score;
    }

    public void incScore() {
        this.score++;
    }

    public int compareTo(TrackScore as) {
        return as.getScore() - this.score;
    }
}