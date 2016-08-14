package DataObjects;

/**
 * Created by Hans on 19-11-2015.
 */
public class ArtistObscure implements Comparable<ArtistObscure>
{
    private String artist;
    private int listeners;
    private int score;

    public ArtistObscure(String _artist, int _listeners) {
        this.artist = _artist;
        this.listeners = _listeners;
        this.score = getPoints(listeners);
    }

    public String getArtist() {
        return artist;
    }

    public int getListeners() {
        return listeners;
    }

    public int getScore() {
        return score;
    }

    public int compareTo(ArtistObscure as) {
        return as.getListeners() - this.listeners;
    }

    private int getPoints(int listeners) {
        if (listeners <= 2000) {
            return 10;
        } else if (listeners > 2000 & listeners <= 15000) {
            return 7;
        } else if (listeners > 15000 & listeners <= 50000) {
            return 5;
        } else if (listeners > 50000 & listeners <= 100000) {
            return 3;
        } else if (listeners > 100000 & listeners <= 500000) {
            return 2;
        } else if (listeners > 500000 & listeners <= 1000000) {
            return 1;
        } else {
            return 0;
        }
    }

    public String toString() {
        return artist + ": " + listeners;
    }
}
