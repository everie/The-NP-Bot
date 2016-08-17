package Commands.Sub;

import DataObjects.ArtistObscure;
import DataObjects.BotInfo;
import DataObjects.NickInfo;
import Tools.Info;
import Tools.Toolbox;
import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Hans on 15-08-2016.
 */
public class Obscure {

    Toolbox toolBox = new Toolbox();
    BotInfo info = Info.getInfo();
    private ArrayList<ArtistObscure> artistList = new ArrayList<>();

    public String getOutput(String interval, NickInfo nickInfo) {

        String ss = info.getSplit();
        String api = info.getApiLastFM();
        String nick = nickInfo.getLfmNick();

        String description;

        switch (interval) {
            case "7day":
                description = "Last week";
                break;

            case "1month":
                description = "Last month";
                break;

            case "12month":
                description = "Last year";
                break;

            default:
                description = "Overall";
                break;
        }

        try {
            String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user=" + nick + "&api_key=" + api + "&format=json&limit=10&period=" + interval);
            JSONObject obj = new JSONObject(input);

            JSONObject artists = obj.getJSONObject("topartists");
            JSONArray artist = artists.getJSONArray("artist");

            for (int i = 0; i < artist.length(); i++) {
                JSONObject current = artist.getJSONObject(i);

                String currentArtist = current.getString("name");
                String inputArtist = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + URLEncoder.encode(currentArtist, "UTF-8") + "&api_key=" + api + "&format=json");

                JSONObject objArtist = new JSONObject(inputArtist);
                int listeners = objArtist.getJSONObject("artist").getJSONObject("stats").getInt("listeners");

                artistList.add(new ArtistObscure(currentArtist, listeners));
            }

            Collections.sort(artistList, Comparator.reverseOrder());

            int points = 0;
            String dispObscure = "";

            for (int i = 0; i < artistList.size(); i++) {
                ArtistObscure ao = artistList.get(i);
                if (i < 3) {
                    if (i > 0) {
                        dispObscure += " " + ss + " ";
                    }
                    dispObscure += ao.getArtist() + " (" + ao.getListeners() + " listeners)";
                }
                points += ao.getScore();
            }

            return description + " " + displayNick(nickInfo) + " has been " + Colors.BOLD + points + "%" + Colors.NORMAL + " hipster " + ss + " Most significant artists: " + dispObscure;


        } catch (IOException |JSONException e) {
            return "Something went wrong. " + e.getMessage();
        }

    }

    private String displayNick(NickInfo ni) {
        if (ni.getIsReg()) {
            return Colors.BOLD + toolBox.escapeName(ni.getIrcNick()) + Colors.NORMAL;
        } else {
            return ni.getLfmNick();
        }
    }

}
