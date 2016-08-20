package DataObjects;

import Tools.Toolbox;

import java.io.Serializable;

/**
 * Created by Hans on 19-11-2015.
 */
public class ArtistObscure implements Comparable<ArtistObscure>, Serializable
{
    private String artist;
    private int listeners;
    private long expire;

    public ArtistObscure(String _artist, int _listeners, long _expire) {
        this.artist = _artist;
        this.listeners = _listeners;
        this.expire = _expire;
    }

    public String getArtist() {
        return artist;
    }

    public int getListeners() {
        return listeners;
    }

    public long getExpire() {
        return expire;
    }

    public int compareTo(ArtistObscure as) {
        return as.getListeners() - this.listeners;
    }

    public String toString() {
        return "Listeners: " + listeners;
    }
}
