package Commands;

import DataObjects.UserScore;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hans on 28-07-2016.
 */
public class ArtistTop extends AbstractCommand {

    private String artist;
    private static long lastUsed = 0;
    private int wait = 10;

    protected boolean handleParams(String[] params) {

        if (params.length > 0) {
            artist = arrayToString(params, 0);
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "TOPARTIST";
        String explain = "Displays a top list of a specific Artist's plays based on online users in a channel. " + info.getSplit();
        return explain + " Usage: " + cmd + " <Artist Name>";
    }

    public String getOutput() {
        Database.User user = new Database.User();
        String ss = info.getSplit();
        String api = info.getApiLastFM();

        if (Instant.now().getEpochSecond() - wait > lastUsed)
        {
            lastUsed = Instant.now().getEpochSecond();
        } else {
            return "Please allow " + wait + " seconds to pass inbetween use of -topartist";
        }

        List<String> online = new ArrayList<>();
        for (User u : info.getBot().getUsers(channel)) {
            online.add(u.getNick());
        }

        int sum = 0;
        String artName = "";

        List<UserScore> list = user.getAllLastFM(online);

        for (int x = 0; x < list.size(); x++) {
            UserScore us = list.get(x);
            String lastfm = us.getLastFM();
            int plays = 0;

            try
            {
                String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + URLEncoder.encode(artist, "UTF-8") + "&api_key=" + api + "&format=json&autocorrect=1&username=" + lastfm);

                JSONObject obj = new JSONObject(input);
                JSONObject art = obj.getJSONObject("artist");
                JSONObject stats = art.getJSONObject("stats");

                artName = art.getString("name");
                plays = stats.getInt("userplaycount");
                sum += plays;

            } catch (IOException|JSONException e) {
                plays = 0;
            }

            us.setScore(plays);
        }

        Collections.sort(list);

        String listScores = "";
        int itr = 0;

        for (UserScore us : list) {
            if (us.getScore() > 0)
            {
                String name = toolBox.escapeName(us.getNick());

                if (itr == 0)
                {
                    listScores += name + ": " + us.getScore();
                } else
                {
                    listScores += " " + ss + " " + name + ": " + us.getScore();
                }
                if (itr < 3) {
                    int pct = (int) Math.round((double)us.getScore() * 100.0 / (double)sum);
                    listScores += " (" + pct + "%)";
                }
            }
            itr++;
        }

        return "Top " + Colors.BOLD + artName + Colors.NORMAL + " Scrobblers " + ss + " Total: " + sum + " " + ss + " " + listScores;
    }
}
