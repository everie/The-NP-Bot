package Commands;

import DataObjects.NickInfo;
import Database.User;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Hans on 28-07-2016.
 */
public class NowWatching extends AbstractCommand {

    private NickInfo nickInfo = new NickInfo();

    protected boolean handleParams(String[] params) {

        boolean isSelf = false;
        if (params.length > 0) {
            setNickInfo(params[0], nickInfo, "trakt");
        } else {
            setNickInfo(sender, nickInfo, "trakt");
            isSelf = true;
        }

        if (isSelf && !nickInfo.getIsReg()) {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "TV";
        String explain = "Displays what a given user is currently watching. " + info.getSplit();
        return explain + " Usage: " + cmd + " <IRC Nick|Trakt.tv Nick> OR " + cmd + " alone for registered irc nicks.";
    }

    public String getOutput() {
        String nick = nickInfo.getLfmNick();
        String ss = info.getSplit();

        try
        {

            String input = getData("https://api-v2launch.trakt.tv/users/" + nick + "/watching");

            JSONObject obj = new JSONObject(input);
            String type = obj.getString("type");
            String npUser = displayNick(nickInfo) + " is watching: ";

            switch (type) {
                case "episode":
                    JSONObject ep = obj.getJSONObject("episode");
                    JSONObject show = obj.getJSONObject("show");

                    JSONObject id = ep.getJSONObject("ids");

                    String name = show.getString("title");
                    int episode = ep.getInt("number");
                    int season = ep.getInt("season");
                    String title = ep.getString("title");
                    int tid = id.getInt("trakt");
                    String trakt = toolBox.shortURL("https://trakt.tv/episodes/" + tid);

                    String e;
                    if (episode < 10) {
                        e = "0" + String.valueOf(episode);
                    } else {
                        e = String.valueOf(episode);
                    }
                    String epDisp = season + "x" + e;

                    String out = npUser + name + " " + epDisp + ": " + title + " " + ss + " " + trakt;

                    return out;

                case "movie":

                    JSONObject mv = obj.getJSONObject("movie");
                    JSONObject ids = mv.getJSONObject("ids");

                    String mv_title = mv.getString("title");
                    int mv_year = mv.getInt("year");
                    int mv_tid = ids.getInt("trakt");

                    String mv_trakt = toolBox.shortURL("https://trakt.tv/movies/" + mv_tid);

                    String mv_out = npUser + mv_title + " (" + mv_year + ") - " + mv_trakt;

                    return mv_out;
            }

            return "";

        } catch (JSONException e) {
            if (e.getMessage().equals("A JSONObject text must begin with '{' at 1 [character 2 line 1]")) {
                return "Nothing is scrobbling for user " + displayNick(nickInfo) + ".";
            } else
            {
                return "Something went wrong. " + e.getMessage();
            }
        }

    }


    public String getData(String link) {

        String api = info.getApiTraktTV();

        try
        {
            URL url = new URL(link);
            HttpsURLConnection con;

            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("trakt-api-version", "2");
            con.setRequestProperty("trakt-api-key", api);

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
            {
                responseStrBuilder.append(inputStr);
            }

            streamReader.close();

            return responseStrBuilder.toString();
        }catch (IOException e)
        {
            return "Something went wrong. " + e.getMessage();
        }
    }

    /*
    protected void setNickInfo(String nick, NickInfo ni) {
        User user = new User();
        String type = "trakt";

        if (user.isUser(nick, type)) {
            ni.setisReg(true);
            ni.setIrcNick(nick);
            ni.setLfmNick(user.getUser(nick, type));
        } else {
            ni.setLfmNick(nick);
            ni.setisReg(false);
        }
    }
    */
}
