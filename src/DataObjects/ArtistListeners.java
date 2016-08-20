package DataObjects;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created by Hans on 20-08-2016.
 */
public class ArtistListeners implements Serializable {

    private TreeMap<String, ArtistObscure> map = new TreeMap<>();

    public TreeMap<String, ArtistObscure> getMap() {
        return map;
    }
}
