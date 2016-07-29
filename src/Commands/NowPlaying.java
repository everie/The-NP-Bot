package Commands;

import DataObjects.NickInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Hans on 16-10-2015.
 */
public class NowPlaying extends AbstractCommand
{

    private NickInfo nickInfo = new NickInfo();

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
        String cmd = info.getIdentifier() + "NP";
        String explain = "Got nothing to say? Show your peers what you're listening to instead! " + info.getSplit();
        return explain + " Usage: " + cmd + " <IRC Nick|Last.fm Nick> OR " + cmd + " alone for registered irc nicks.";
    }

    public String getOutput() {

        String ss = info.getSplit();
        String nick = nickInfo.getLfmNick();
        String api = info.getApiLastFM();

        String ret;
        String plays = "n/a";
        String duration = "";
        String totalDuration = "";
        String album = "";
        Boolean hasPlays = true;
        Boolean hasDur = false;

        try
        {
            //String nick = "everieone";

            int pc;

            String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&user=" + nick + "&api_key=" + api + "&format=json&limit=2");
            JSONObject obj = new JSONObject(input);
            JSONObject rec = obj.getJSONObject("recenttracks");
            JSONArray track = rec.getJSONArray("track");
            JSONObject cur = track.getJSONObject(0);

            JSONObject attr = cur.getJSONObject("@attr");

            JSONObject art = cur.getJSONObject("artist");
            String artist = art.getString("#text");
            String song = cur.getString("name");

            //String urlArtist = Json.replacesForUrl(artist);
            //String urlSong = Json.replacesForUrl(song);

            String urlArtist = URLEncoder.encode(artist, "UTF-8");
            String urlSong = URLEncoder.encode(song, "UTF-8");

            String trackInfo = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=" + api + "&artist=" + urlArtist + "&track=" + urlSong + "&format=json&username=" + nick);

            JSONObject trackObj = new JSONObject(trackInfo);

            String tag = "";
            String rank = "";
            String love = "";

            int dur = 0;

            try
            {
                JSONObject trackRec = trackObj.getJSONObject("track");

                JSONObject trackTag = trackRec.getJSONObject("toptags");
                JSONArray tags = trackTag.getJSONArray("tag");

                try {
                    album = " " + ss + " Album: " + trackRec.getJSONObject("album").getString("title");
                } catch (JSONException e) {
                    // do nothing
                }

                try
                {
                    dur = trackRec.getInt("duration");
                    if (dur > 0) {
                        hasDur = true;
                    }
                } catch (JSONException e) {
                    dur = 0;
                }

                int tagLen = tags.length();

                if (tagLen > 0)
                {
                    tag =  " " + ss + " Tags: ";

                    int tagDisp;
                    if (tagLen > 3)
                    {
                        tagDisp = 3;
                    } else
                    {
                        tagDisp = tagLen;
                    }

                    for (int x = 0; x < tagDisp; x++)
                    {
                        String curTag = tags.getJSONObject(x).getString("name");
                        if (x == 0)
                        {
                            tag += curTag;
                        } else
                        {
                            tag += ", " + curTag;
                        }
                    }
                }

                int loved = trackRec.getInt("userloved");

                if (loved == 1)
                {
                    love = "[♥] ";
                }

                try
                {
                    pc = Integer.parseInt(trackRec.getString("userplaycount"));
                } catch (JSONException e)
                {
                    pc = 0;
                    hasPlays = false;
                }


                if (pc > 0)
                {
                    String topInfo = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.gettoptracks&user=" + nick + "&api_key=" + api + "&format=json&limit=1");

                    JSONObject topObj = new JSONObject(topInfo);
                    JSONObject topRec = topObj.getJSONObject("toptracks");
                    JSONArray topTrack = topRec.getJSONArray("track");
                    int tp = Integer.parseInt(topTrack.getJSONObject(0).getString("playcount"));

                    double like = Math.round(10.0 * ((double) pc * 100.0 / (double) tp)) / 10.00;
                    rank = " " + ss + " Rank: " + like + "%";
                }

            } catch (JSONException e) {
                pc = 0;
            }

            if (hasPlays) {
                plays = String.valueOf(pc);
            }

            if (hasDur) {
                duration = " " + ss + " Length: " + toolBox.showTime(dur);

                if (hasPlays && pc > 5)
                {
                    int total = dur * pc;
                    totalDuration = " " + ss + " Playtime: " + toolBox.showTime(total);
                }
            }

            ret = displayNick(nickInfo) + " np: " + love + artist + " - " + song + album + duration + " " + ss + " Plays: " + plays + rank + tag + totalDuration;

        }
        catch(IOException e) {
            ret = "Something went wrong. " + e.getMessage();
        }
        catch(JSONException e) {
            if (e.getMessage().equals("JSONObject[\"@attr\"] not found.")) {
                ret = "Nothing is scrobbling for user " + displayNick(nickInfo) + ".";
            } else
            {
                ret = "Something went wrong. " + e.getMessage();
            }
        }

        return ret;
    }

}
