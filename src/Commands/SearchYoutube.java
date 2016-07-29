package Commands;

import Database.User;
import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Hans on 27-07-2016.
 */
public class SearchYoutube extends AbstractCommand {

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
            query = toolBox.getCurrentTrack(user.getUser(nick, "lastfm"));
            if (query.equals("error")) {
                return false;
            }
        } else {
            query = nick;
        }
        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "YT";
        String explain = "Lets you search on Youtube. If given an IRC Nick it'll search for the users current/last played track. " + info.getSplit();
        return explain + " Usage: " + cmd + " <search query> OR " + cmd + " <IRC Nick>";
    }

    public String getOutput() {
        String ret;
        String ss = info.getSplit();
        String api = info.getApiGoogle();

        try
        {
            String _q = URLEncoder.encode(query, "UTF-8");
            String input = toolBox.apiToString("https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&key=" + api + "&q=" + _q);
            JSONObject obj = new JSONObject(input);
            JSONArray items = obj.getJSONArray("items");
            JSONObject result = items.getJSONObject(0);
            JSONObject snippet = result.getJSONObject("snippet");

            String id = result.getJSONObject("id").getString("videoId");
            String title = snippet.getString("title");
            String desc = snippet.getString("description");

            String link = "https://www.youtube.com/watch?v=" + id;

            String formDesc = desc.replaceAll("\n", "");

            String returned = Colors.BOLD + title + Colors.NORMAL + " " + ss + " " + formDesc + " " + ss + " " + Colors.UNDERLINE + toolBox.shortURL(link);

            ret = returned;

        }
        catch(IOException|JSONException e) {
            ret = "Something went wrong. " + e.getMessage();
        }

        return ret;
    }

}
