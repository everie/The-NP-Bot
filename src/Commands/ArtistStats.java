package Commands;

import DataObjects.NickInfo;
import DataObjects.TrackScore;
import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Hans on 27-07-2016.
 */
public class ArtistStats extends AbstractCommand {

    NickInfo nickInfo = new NickInfo();
    String artist;

    protected boolean handleParams(String[] params) {

        if (params.length > 1) {
            setNickInfo(params[0], nickInfo, "lastfm");
            artist = arrayToString(params, 1);
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "ARTIST";
        String explain = "Displays a user's listening trends for an artist. " + info.getSplit();
        return explain + " Usage: " + cmd + " <IRC Nick|Last.fm Nick> <Artist Name>";
    }

    public String getOutput() {
        String nick = nickInfo.getLfmNick();
        String ss = info.getSplit();
        String api = info.getApiLastFM();

        try
        {
            int trackLim = 200;

            String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + URLEncoder.encode(artist, "UTF-8") + "&api_key=" + api + "&format=json&autocorrect=1&username=" + nick);

            JSONObject obj = new JSONObject(input);
            JSONObject art = obj.getJSONObject("artist");
            JSONObject stats = art.getJSONObject("stats");

            String artName = art.getString("name");

            int pc = 0;
            String rank = "";

            try
            {
                pc = stats.getInt("userplaycount");
            } catch (JSONException e) {
                pc = 0;
            }

            String personal = "";

            if (pc > 0)
            {
                String trackInput = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.getartisttracks&user=" + nick + "&artist=" + URLEncoder.encode(artName, "UTF-8") + "&api_key=" + api + "&format=json&limit=" + trackLim);
                JSONObject trackObj = new JSONObject(trackInput);
                JSONObject artTracks = trackObj.getJSONObject("artisttracks");
                JSONArray tracks = artTracks.getJSONArray("track");

                String topArtist = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user=" + nick + "&api_key=" + api + "&format=json&limit=1");

                JSONObject topArt = new JSONObject(topArtist);

                int topPc = topArt.getJSONObject("topartists").getJSONArray("artist").getJSONObject(0).getInt("playcount");

                double like = Math.round(10.0 * ((double) pc * 100.0 / (double) topPc)) / 10.00;
                rank = " " + ss + " Rank: " + like + "%";

                int tracksLen = tracks.length();
                String tracksDisp = "";

                Map<String, TrackScore> scores = new HashMap<>();

                for (int x = 0; x < tracksLen; x++)
                {
                    String name = tracks.getJSONObject(x).getString("name");
                    if (scores.containsKey(name)) {
                        scores.get(name).incScore();
                    } else {
                        scores.put(name, new TrackScore(name));
                    }
                }

                List<TrackScore> displayScores = new ArrayList<>(scores.values());
                Collections.sort(displayScores);

                int dispTracks = displayScores.size();
                if (dispTracks > 5) {
                    dispTracks = 5;
                }

                for (int i = 0; i < dispTracks; i++)
                {
                    TrackScore ts = displayScores.get(i);
                    String score = " (" + Colors.BOLD + ts.getScore() + Colors.NORMAL + ")";
                    if (i > 0) {
                        tracksDisp += " " + ss + " " + ts.getTrack() + score;
                    } else {
                        tracksDisp += ts.getTrack() + score;
                    }
                }

                String showLast = "";

                if (tracksLen >= trackLim) {
                    showLast = " " + ss + " Last " + tracksLen + " plays";
                }

                personal = displayNick(nickInfo) + "'s plays: " + pc + rank + showLast + " " + ss + " Top " + dispTracks + " of " + displayScores.size() + " tracks " + ss + " " + tracksDisp;

            } else {
                return displayNick(nickInfo) + " hasn't listened to " + artName + ".";
            }

            String ret = artName + " " + ss + " " + personal;

            return ret;


        }catch (IOException|JSONException e) {
            return "Something went wrong. " + e.getMessage();
        }

    }
}
