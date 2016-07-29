package Commands.Sub;

import DataObjects.BotInfo;
import DataObjects.UserScore;
import Enums.Interval;
import Tools.Info;
import Tools.Toolbox;
import org.jibble.pircbot.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hans on 27-07-2016.
 */
public class TopPlays {
    Toolbox toolBox = new Toolbox();
    BotInfo info = Info.getInfo();

    public String getOutput(Interval interval, String channel) {
        Database.User user = new Database.User();
        String ss = info.getSplit();
        String api = info.getApiLastFM();

        long from;
        int days;
        String title;
        switch (interval)
        {
            case TODAY:
                days = 1;
                title = "Top 24h Scrobblers";
                break;

            case WEEK:
                days = 7;
                title = "Top 7days Scrobblers";
                break;

            case MONTH:
                days = 30;
                title = "Top 30days Scrobblers";
                break;

            case YEAR:
                days = 365;
                title = "Top 365days Scrobblers";
                break;

            default:
                days = 0;
                title = "Top Overall Scrobblers";
                break;
        }

        from = toolBox.getFromInterval(days);

        List<String> online = new ArrayList<>();
        for (User u : info.getBot().getUsers(channel)) {
            online.add(u.getNick());
        }

        List<UserScore> list = user.getAllLastFM(online);
        int sum = 0;

        for (int x = 0; x < list.size(); x++) {
            UserScore us = list.get(x);
            String lastfm = us.getLastFM();
            int plays = 0;
            try
            {
                String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&user=" + lastfm + "&api_key=" + api + "&format=json&limit=1&from=" + from);
                JSONObject obj = new JSONObject(input);
                JSONObject recent = obj.getJSONObject("recenttracks").getJSONObject("@attr");
                plays = recent.getInt("total");
                sum += plays;

                //System.out.println(plays);

            } catch (IOException |JSONException e) {
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
                    listScores += " • " + name + ": " + us.getScore();
                }

                if (itr < 3) {
                    int pct = (int) Math.round((double)us.getScore() * 100.0 / (double)sum);
                    listScores += " (" + pct + "%)";
                }
            }
            itr++;
        }

        return channel.toUpperCase() + " " + title + " " + ss + " Total: " + sum + " " + ss + " " + listScores;
    }
}
