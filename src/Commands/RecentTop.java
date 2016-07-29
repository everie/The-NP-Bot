package Commands;

import DataObjects.NickInfo;
import DataObjects.RecentScore;
import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Hans on 28-07-2016.
 */
public class RecentTop extends AbstractCommand {

    private NickInfo nickInfo = new NickInfo();
    private HashMap<String, HashMap<String, RecentScore>> trackMap;
    private ArrayList<RecentScore> tracks;

    protected boolean handleParams(String[] params) {

        boolean isSelf = false;
        if (params.length > 0) {
            setNickInfo(params[0], nickInfo, "lastfm");
        } else {
            setNickInfo(sender, nickInfo, "lastfm");
            isSelf = true;
        }

        if (isSelf && !nickInfo.getIsReg()) {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "RECENT";
        String explain = "Displays a user's recent listening trends. " + info.getSplit();
        return explain + " Usage: " + cmd + " <IRC Nick|Last.fm Nick> OR " + cmd + " alone for registered irc nicks.";
    }

    public String getOutput() {
        String nick = nickInfo.getLfmNick();
        String api = info.getApiLastFM();
        String ss = info.getSplit();

        try {
            String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&user=" + nick + "&api_key=" + api + "&format=json&limit=200");
            JSONObject obj = new JSONObject(input);
            JSONObject rec = obj.getJSONObject("recenttracks");
            JSONArray track = rec.getJSONArray("track");

            int size = track.length();
            tracks = new ArrayList<>();
            trackMap = new HashMap<>();

            for (int i = 0; i < size; i++) {
                JSONObject cur = track.getJSONObject(i);

                if (i == 0) {

                    try {
                        JSONObject attr = cur.getJSONObject("@attr");
                    } catch (JSONException e) {
                        addTrack(cur);
                    }
                } else {
                    addTrack(cur);
                }
            }

            Collections.sort(tracks);

            String output = "";
            int max = 5;

            if (tracks.size() < max) {
                max = tracks.size();
            }

            for (int i = 0; i < max; i++) {
                RecentScore rs = tracks.get(i);
                if (i > 0) {
                    output += " " + ss + " ";
                }
                output += rs.getArtist() + " - " + rs.getTrack() + " (" + Colors.BOLD + rs.getScore() + Colors.NORMAL + ")";
            }

            return "Top " + max + " Tracks of last 200 Plays " + ss + " " + output;

        } catch (IOException|JSONException e) {
            return "Something went wrong. " + e.getMessage();
        }

    }

    private void addTrack(JSONObject obj) {
        String track = obj.getString("name");
        String artist = obj.getJSONObject("artist").getString("#text");
        if (!trackMap.containsKey(artist)) {
            HashMap<String, RecentScore> newMap = new HashMap<>();
            RecentScore rs = new RecentScore(artist, track);
            newMap.put(track, rs);
            tracks.add(rs);
            trackMap.put(artist, newMap);
        } else {
            if (!trackMap.get(artist).containsKey(track)) {
                RecentScore rs = new RecentScore(artist, track);
                trackMap.get(artist).put(track, rs);
                tracks.add(rs);
            } else {
                trackMap.get(artist).get(track).incScore();
            }
        }
    }
}
