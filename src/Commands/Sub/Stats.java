package Commands.Sub;

import DataObjects.BotInfo;
import DataObjects.NickInfo;
import Tools.Info;
import Tools.Toolbox;
import org.jibble.pircbot.Colors;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;

/**
 * Created by Hans on 26-07-2016.
 */
public class Stats {

    Toolbox toolBox = new Toolbox();
    BotInfo info = Info.getInfo();

    public String getOutput(String interval, NickInfo nickInfo) {

        String nick = nickInfo.getLfmNick();
        String ss = info.getSplit();
        String api = info.getApiLastFM();

        String ret;
        long from;
        int days;
        double regDays = 0;
        int regDaysClean = 0;

        switch (interval) {
            case "7day":
                days = 7;
                break;

            case "1month":
                days = 30;
                break;

            case "12month":
                days = 365;
                break;

            default:
                days = 0;
                break;
        }
        from = toolBox.getFromInterval(days);

        try
        {
            String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.getinfo&user=" + nick + "&api_key=" + api + "&format=json");
            String recentInfo = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&user=" + nick + "&api_key=" + api + "&format=json&limit=1&from=" + from);
            String trackInfo = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.gettoptracks&user=" + nick + "&api_key=" + api + "&format=json&limit=1&period=" + interval);
            String artistInfo = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user=" + nick + "&api_key=" + api + "&format=json&limit=1&period=" + interval);

            JSONObject obj = new JSONObject(input);
            JSONObject user = obj.getJSONObject("user");
            JSONObject reg = user.getJSONObject("registered");

            // RECENT
            JSONObject recObj = new JSONObject(recentInfo);
            JSONObject recAttr = recObj.getJSONObject("recenttracks").getJSONObject("@attr");
            int pc = recAttr.getInt("total");

            //int pc = Integer.parseInt(user.getString("playcount"));

            long regDate = Integer.parseInt(reg.getString("unixtime"));

            String dispLoves;

            if (days == 0)
            {

                String inputLoves = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.getlovedtracks&user=" + nick + "&api_key=" + api + "&format=json&limit=1");
                int loves = new JSONObject(inputLoves).getJSONObject("lovedtracks").getJSONObject("@attr").getInt("total");

                dispLoves = loves + " loved tracks, ";

                long nowDate = Instant.now().getEpochSecond();

                long regTime = nowDate - regDate;
                long msToDays = (60 * 60 * 24);

                regDays = (double) regTime / (double) msToDays;
                regDaysClean = (int) regDays;
            } else {
                regDays = days;
                regDaysClean = days;

                dispLoves = "";
            }

            double pcDays = Math.round((double)pc*100.00 / regDays)/100.00;

            // TRACKS
            JSONObject topObj = new JSONObject(trackInfo);
            JSONObject topTrack = topObj.getJSONObject("toptracks");
            JSONObject topAttr = topTrack.getJSONObject("@attr");

            int tracks = topAttr.getInt("total");

            double tracksDays = Math.round((double)tracks*100.00 / regDays)/100.00;
            double tracksPc = Math.round((double)pc*100.00 / tracks)/100.00;

            // ARTISTS
            JSONObject artObj = new JSONObject(artistInfo);
            JSONObject artTrack = artObj.getJSONObject("topartists");
            JSONObject artAttr = artTrack.getJSONObject("@attr");

            int artists = artAttr.getInt("total");

            double artistsDays = Math.round((double)artists*100.00 / regDays)/100.00;
            double artistsTracks = Math.round((double)tracks*100.00 / artists)/100.00;
            double artistsPc = Math.round((double)pc*100.00 / artists)/100.00;

            //System.out.println(artists);

            String showReg = "";
            if (interval.equals("overall")) {
                showReg = " (" + toolBox.getDateFromTS(regDate) + ")";
            }

            String showDays = Colors.BOLD + "General: " + Colors.NORMAL + regDaysClean + " days" + showReg + ", " + dispLoves + pc + " scrobbles, " + pcDays + " scrobbles/day";
            String showArtists = Colors.BOLD + "Artists: " + Colors.NORMAL + artists + " unique artists, " + artistsDays + " new artists/day, " + artistsPc + " scrobbles/artist";
            String showTracks = Colors.BOLD + "Tracks: " + Colors.NORMAL + tracks + " unique tracks, " + tracksDays + " new tracks/day, " + tracksPc + " scrobbles/track, " + artistsTracks + " tracks/artist.";

            ret = showDays + " " + ss + " " + showArtists + " " + ss + " " + showTracks;

        }
        catch(IOException|JSONException e) {
            ret = "Something went wrong. " + e.getMessage();
        }

        return ret;
    }
}
