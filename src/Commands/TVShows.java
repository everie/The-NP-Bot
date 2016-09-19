package Commands;

import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hans on 18-09-2016.
 */
public class TVShows extends AbstractCommand {

    String query;

    protected boolean handleParams(String[] params) {

        if (params.length > 0) {
            query = arrayToString(params, 0);
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        return null;
    }

    public String getOutput() {
        String ret;
        String ss = info.getSplit();
        long now = Instant.now().getEpochSecond();

        try
        {
            String input = toolBox.apiToString("http://api.tvmaze.com/singlesearch/shows?q=" + URLEncoder.encode(query, "UTF-8") + "&embed=episodes");

            JSONObject obj = new JSONObject(input);
            String name = obj.getString("name");
            String genres = getStringOfArray(obj.getJSONArray("genres"));

            String status = obj.getString("status");

            JSONArray episodes = obj.getJSONObject("_embedded").getJSONArray("episodes");

            ret = name + genres + " " + ss + " Status: " + status;

            boolean nextFound = false;
            String next = "";
            int nextEpisode = 0;

            for (int i = 0; i < episodes.length(); i++) {
                JSONObject ep = episodes.getJSONObject(i);
                String airDate = ep.getString("airdate");
                String airTime = ep.getString("airtime");
                String airShow = airDate + " " + airTime;
                long airStamp = toolBox.getTimeStampFromDate(airShow);

                if (airStamp > now) {
                    String duration = showDuration((airStamp - now) * 1000);
                    String title = ep.getString("name");
                    int season = ep.getInt("season");
                    int episode = ep.getInt("number");

                    next = " " + ss + Colors.BOLD + " Next: " + Colors.NORMAL + title + " (" + getSeasonEpisode(season, episode) + "), Air: " + airShow + " (" + duration + ")";

                    // getDateFromTS(airStamp) to get timezone

                    nextEpisode = i;
                    nextFound = true;
                    break;
                }
            }

            if (nextFound) {
                ret += getLastEpisode((JSONObject) episodes.get(nextEpisode - 1));

                ret += next;
            } else {
                ret += getLastEpisode((JSONObject) episodes.get(episodes.length() - 1));

                if (status.equals("Running")) {
                    ret += " " + ss + Colors.BOLD + " Next: " + Colors.NORMAL + "TBA";
                }
            }


        }
        catch(IOException |JSONException e) {
            ret = "Something went wrong. " + e.getMessage();
        }

        return ret;
    }

    private String getSeasonEpisode(int season, int episode) {
        String dispEpisode;
        if (episode > 9) {
            dispEpisode = String.valueOf(episode);
        } else {
            dispEpisode = "0" + episode;
        }
        String dispSeason;
        if (season > 9) {
            dispSeason = String.valueOf(season);
        } else {
            dispSeason = "0" + season;
        }

        return Colors.BOLD + "S" + Colors.NORMAL + dispSeason + Colors.BOLD + "E" + Colors.NORMAL + dispEpisode;
    }

    private String getStringOfArray(JSONArray array) {
        if (array.length() < 1) {
            return "";
        }
        String out = " " + info.getSplit() + " Genres: ";
        for (int i = 0; i < array.length(); i++) {
            if (i > 0) {
                out += ", ";
            }
            out += array.get(i);
        }
        return out;
    }

    private String getLastEpisode(JSONObject ep) {
        String airDate = ep.getString("airdate");
        String title = ep.getString("name");
        int season = ep.getInt("season");
        int episode = ep.getInt("number");

        return  " " + info.getSplit() + Colors.BOLD + " Last: " + Colors.NORMAL + title + " (" + getSeasonEpisode(season, episode) + "), Aired: " + airDate;
    }

    public String showDuration(long dur) {
        String ret;

        long hour = 3600000;
        long day = hour * 24;

        if (dur < hour) {
            ret = String.format("%dm, %ds",
                    TimeUnit.MILLISECONDS.toMinutes(dur),
                    TimeUnit.MILLISECONDS.toSeconds(dur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur))
            );
        } else if (dur > hour && dur < day) {
            ret = String.format("%dh",
                    TimeUnit.MILLISECONDS.toHours(dur)
            );
        } else {
            ret = String.format("%dd, %dh",
                    TimeUnit.MILLISECONDS.toDays(dur),
                    TimeUnit.MILLISECONDS.toHours(dur) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(dur))
            );
        }

        return ret;
    }

    /*
    public String getDateFromTS(long ts)
    {
        java.util.Date _date = new java.util.Date(ts * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm zzz");
        return sdf.format(_date);
    }
    */
}
