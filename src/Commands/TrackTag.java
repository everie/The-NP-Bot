package Commands;

import DataObjects.NickInfo;
import Database.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Hans on 17-08-2016.
 */
public class TrackTag extends AbstractCommand {

    private NickInfo nickInfo = new NickInfo();
    private String api = info.getApiLastFM();
    private String secret = info.getApiLastFMSecret();
    private String method = "track.addTags";

    private String npArtist;
    private String npTrack;

    private ArrayList<String> tagList;

    protected boolean handleParams(String[] params) {
        if (params.length > 0) {

            String tags = "";
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    tags += " ";
                }
                tags += params[i];
            }

            tagList = new ArrayList<>();

            for (String t : tags.split(",")) {
                if (t.replaceAll(" ", "").length() > 0) {
                    String tempTag = t.trim();

                    if (tagList.size() < 10) {
                        tagList.add(tempTag);
                    } else {
                        break;
                    }
                }
            }

            User user = new User();

            setNickInfo(sender, nickInfo, "lastfm");
            if (nickInfo.getIsReg()) {
                String auth = user.getUserAuth(sender, hostname);

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
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "TAG";
        String explain = "Lets you tag your currently playing track. " + info.getSplit();
        return explain + " Usage: " + cmd + " for authenticated IRC Nicks.";
    }

    public String getOutput() {

        URL url;
        HttpURLConnection connection = null;
        String key = nickInfo.getAuthKey();
        String ss = info.getSplit();

        try {

            String npTags = "";
            for (int i = 0; i < tagList.size(); i++) {
                if (i > 0) {
                    npTags += ",";
                }
                npTags += tagList.get(i);
            }

            String sig = getSignature(method, npArtist, npTrack, key, npTags);

            //Create connection
            url = new URL("http://ws.audioscrobbler.com/2.0/");

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("method", method);
            params.put("artist", npArtist);
            params.put("track", npTrack);
            params.put("tags", npTags);
            params.put("api_key", api);
            params.put("api_sig", sig);
            params.put("sk", key);

            //System.out.println(params);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(param.getKey());
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
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

        String displayTags = "";
        for (int i = 0; i < tagList.size(); i++) {
            if (i > 0) {
                displayTags += " " + ss + " ";
            }
            displayTags += tagList.get(i);
        }

        return displayNick(nickInfo) + " tagged " + npArtist + " - " + npTrack + " with: " + displayTags;
    }

    private String getSignature(String method, String a, String t, String k, String tags) {
        String sig = "api_key" + api +
                "artist" + a +
                "method" + method +
                "sk" + k +
                "tags" + tags +
                "track" + t +
                secret;
        return toolBox.getMD5(sig);
    }

}
