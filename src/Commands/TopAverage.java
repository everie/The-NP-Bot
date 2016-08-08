package Commands;

import Commands.Sub.TopPlays;
import DataObjects.UserScore;
import Enums.Interval;
import org.jibble.pircbot.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hans on 26-07-2016.
 */
public class TopAverage extends AbstractCommand {

    protected boolean handleParams(String[] params) {
        return true;
    }

    public String getOutput() {
        Database.User user = new Database.User();
        String ss = info.getSplit();
        String api = info.getApiLastFM();
        long now = Instant.now().getEpochSecond();

        List<String> online = new ArrayList<>();
        for (User u : info.getBot().getUsers(channel)) {
            online.add(u.getNick());
        }

        List<UserScore> list = user.getAllLastFM(online);

        for (int x = 0; x < list.size(); x++) {
            UserScore us = list.get(x);
            String lastfm = us.getLastFM();
            int score = 0;

            try
            {
                String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.getinfo&user=" + lastfm + "&api_key=" + api + "&format=json");
                JSONObject obj = new JSONObject(input);
                JSONObject userObj = obj.getJSONObject("user");
                int plays = userObj.getInt("playcount");
                long reg = userObj.getJSONObject("registered").getLong("unixtime");
                long days = (now - reg) / (60 * 60 * 24);
                score = (int)Math.round((double)plays / (double)days);

            } catch (IOException |JSONException e) {
                score = 0;
            }

            us.setScore(score);
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
                    listScores += " • " + name + ": " + us.getScore();
                }

            }
            itr++;
        }

        return channel.toUpperCase() + " Top Average Daily Scrobblers " + ss + " " + listScores;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "TOPAVERAGE";
        String explain = "Displays a scrobbles pr day top list of all users currently online in a channel. " + info.getSplit();
        return explain + " Usage: " + cmd;
    }
}
