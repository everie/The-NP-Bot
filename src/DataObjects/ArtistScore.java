package DataObjects;

/**
 * Created by Hans on 19-11-2015.
 */
public class ArtistScore implements Comparable<ArtistScore>
{
    private String artist;
    private double score;

    public ArtistScore(String _artist, double _score) {
        this.artist = _artist;
        this.score = _score;
    }

    public String getArtist() {
        return artist;
    }

    public double getScore() {
        return score;
    }

    public int compareTo(ArtistScore as) {
        return (int)(as.getScore()*1000) - (int)(this.score*1000);
    }
}
