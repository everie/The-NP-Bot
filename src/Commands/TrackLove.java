package Commands;

import DataObjects.NickInfo;
import Database.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Hans on 17-08-2016.
 */
public class TrackLove extends AbstractCommand {

    private NickInfo nickInfo = new NickInfo();
    private String api = info.getApiLastFM();
    private String secret = info.getApiLastFMSecret();
    private String method;

    private String npArtist;
    private String npTrack;

    private boolean love;

    public TrackLove(boolean love) {
        this.love = love;
        if (love) {
            method = "track.love";
        } else {
            method = "track.unlove";
        }
    }

    protected boolean handleParams(String[] params) {
        User user = new User();

        setNickInfo(sender, nickInfo, "lastfm");
        if (nickInfo.getIsReg()) {
            String auth = user.getUserAuth(sender);

            if (auth != null) {

                HashMap<String, String> map = toolBox.getCurrentTrack(nickInfo.getLfmNick());

                if (map != null) {
                    npArtist = map.get("artist");
                    npTrack = map.get("track");
                    nickInfo.setIrcNick(sender);
                    nickInfo.setAuthKey(auth);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "LOVE" + "/" + info.getIdentifier() + "UNLOVE";
        String explain = "Lets you love/unlove your currently playing track. " + info.getSplit();
        return explain + " Usage: " + cmd + " for authenticated IRC Nicks.";
    }

    public String getOutput() {

        URL url;
        HttpURLConnection connection = null;
        String key = nickInfo.getAuthKey();

        try {

            String sig = getSignature(method, npArtist, npTrack, key);

            //Create connection
            url = new URL("http://ws.audioscrobbler.com/2.0/");

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("method", method);
            params.put("artist", npArtist);
            params.put("track", npTrack);
            params.put("api_key", api);
            params.put("api_sig", sig);
            params.put("sk", key);

            //System.out.println(params);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(param.getKey());
                postData.append('=');
                postData.append(String.valueOf(param.getValue()));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);


            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));



        } catch (Exception e) {

            return "Something went wrong. " + e.getMessage();

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

        String action;
        if (love) {
            action = "officially loves";
        } else {
            action = "no longer loves";
        }

        return displayNick(nickInfo) + " " + action + " " + npArtist + " - " + npTrack + ".";

    }

    private String getSignature(String method, String a, String t, String k) {
        String sig = "api_key" + api +
                "artist" + a +
                "method" + method +
                "sk" + k +
                "track" + t +
                secret;
        return toolBox.getMD5(sig);
    }
}
