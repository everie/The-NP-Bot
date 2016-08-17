package Commands;

import Database.User;
import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Hans on 27-07-2016.
 */
public class SearchSpotify extends AbstractCommand {

    String query;

    protected boolean handleParams(String[] params) {

        if (params.length == 1) {
            return getTrackFromUser(params[0]);
        } else if (params.length < 1) {
            return getTrackFromUser(sender);
        } else if (params.length > 1) {
            query = arrayToString(params, 0);
        } else {
            return false;
        }

        return true;
    }

    private boolean getTrackFromUser(String nick) {
        User user = new User();
        if (user.isUser(nick, "lastfm")) {
            HashMap<String, String> map = toolBox.getCurrentTrack(user.getUser(nick, "lastfm"));

            if (map != null) {
                query = map.get("artist") + " - " + map.get("track");
            } else {
                return false;
            }
        } else {
            query = nick;
        }
        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "SPOT";
        String explain = "Lets you search for a track in Spotify's library. If given an IRC Nick it'll search for the users current track. " + info.getSplit();
        return explain + " Usage: " + cmd + " <search query> OR " + cmd + " <IRC Nick>";
    }

    public String getOutput() {

        String ret;
        String artist = "";
        String length = "";
        String ss = info.getSplit();

        try {
            String _q = URLEncoder.encode(query, "UTF-8");
            String input = toolBox.apiToString("https://api.spotify.com/v1/search?q=" + _q + "&type=track&limit=1");

            JSONObject obj = new JSONObject(input);
            JSONObject tracks = obj.getJSONObject("tracks");
            JSONArray item = tracks.getJSONArray("items");
            JSONObject current = item.getJSONObject(0);

            JSONArray artists = current.getJSONArray("artists");

            for (int i = 0; i < artists.length(); i++) {
                String a = artists.getJSONObject(i).getString("name");
                if (i == 0) {
                    artist = a;
                } else {
                    artist += ", " + a;
                }
            }

            String name = current.getString("name");
            try {
                int dur = current.getInt("duration_ms");
                length = " " + ss + " Length: " + toolBox.showTime(dur);
            } catch (JSONException e) {
                // do nothing
            }

            String uri = current.getString("uri");
            String preview = current.getString("preview_url");

            String returned = artist + " - " + name + length + " " + ss + " Open: " + Colors.UNDERLINE + toolBox.shortSpotURL(uri) + Colors.NORMAL + " " + ss + " Preview: " + Colors.UNDERLINE + toolBox.shortURL(preview);

            ret = returned;

        } catch (IOException e) {
            ret = "Something went wrong. " + e.getMessage();
        } catch (JSONException e) {
            if (e.getMessage().equals("JSONArray[0] not found.")) {
                ret = "No matches found for \"" + query + "\".";
            } else {
                ret = "Something went wrong. " + e.getMessage();
            }
        }

        return ret;
    }

}
