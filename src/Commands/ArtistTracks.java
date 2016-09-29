package Commands;

import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Hans on 29-09-2016.
 */
public class ArtistTracks extends AbstractCommand {

    String artist;

    @Override
    protected boolean handleParams(String[] params) {

        if (params.length > 0) {
            artist = arrayToString(params, 0);
        } else {
            return false;
        }

        return true;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getOutput() {
        String ss = info.getSplit();
        String api = info.getApiLastFM();

        try {
            artist = URLEncoder.encode(artist, "UTF-8");

            String inputArt = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + artist + "&api_key=" + api + "&format=json&autocorrect=1");
            JSONObject objArt = new JSONObject(inputArt).getJSONObject("artist");

            String artistName = objArt.getString("name");
            int artistListeners = objArt.getJSONObject("stats").getInt("listeners");
            //int artistPlays = objArt.getJSONObject("stats").getInt("playcount");

            String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=" + artist + "&api_key=" + api + "&format=json&limit=5");
            JSONArray tracks = new JSONObject(input).getJSONObject("toptracks").getJSONArray("track");

            String dispTracks = "";

            for (int i = 0; i < tracks.length(); i++) {
                JSONObject track = tracks.getJSONObject(i);

                String trackName = track.getString("name");
                //int trackPlays = track.getInt("playcount");
                int trackListeners = track.getInt("listeners");

                /*
                if (i > 0) {
                    dispTracks += " " + ss + " ";
                }
                */



                dispTracks += " " + ss + " " + Colors.BOLD + (i + 1) + Colors.NORMAL + ": " + trackName + " " + ss + " " + trackListeners + " (" + getPct(trackListeners, artistListeners) + "%)";

                /*
                dispTracks += trackName + " " + ss + Colors.DARK_GRAY +
                        " L: " + Colors.BOLD + trackListeners + Colors.NORMAL + Colors.DARK_GRAY + " (" + getPct(trackListeners, artistListeners) + "%), " +
                        "P: " + Colors.BOLD + trackPlays + Colors.NORMAL + Colors.DARK_GRAY + " (" + getPct(trackPlays, artistPlays) + "%)" + Colors.NORMAL;
                        */
            }

            //return artistName + " " + ss + " Listeners: " + artistListeners + ", Plays: " + artistPlays + " " + ss + " Top 5 Tracks " + ss + " " + dispTracks;

            return artistName + " " + ss + " Listeners: " + artistListeners + " " + ss + " Top 5 Tracks" + dispTracks;

        } catch (IOException|JSONException e) {
            return "Something went wrong. " + e.getMessage();
        }
    }

    private int getPct(int trackCount, int totalCount) {
        return (int)Math.round((double) trackCount * 100.0 / (double) totalCount);
    }
}
