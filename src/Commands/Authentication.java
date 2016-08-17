package Commands;

import DataObjects.NickInfo;
import Database.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Hans on 28-07-2016.
 */
public class Authentication extends AbstractCommand {

    private String api = info.getApiLastFM();
    private String secret = info.getApiLastFMSecret();
    private static String token;
    private String stage;

    public Authentication(String stage) {
        this.stage = stage;
    }

    public boolean instantiate(String[] params, String sender) {
        this.params = params;
        this.sender = sender;

        boolean success = handleParams(params);

        return success;
    }

    protected boolean handleParams(String[] params) {
        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "EXCUSE";
        String explain = "Want to make it seem like you know what you're doing even though you're clueless? " + info.getSplit();
        return explain + " Usage: " + cmd;
    }

    public String getOutput() {

        try {
            if (stage.equals("token")) {
                String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=auth.gettoken&api_key=" + api + "&format=json");
                JSONObject inputObj = new JSONObject(input);
                token = inputObj.getString("token");

                return toolBox.shortURL("http://www.last.fm/api/auth/?api_key=" + api + "&token=" + token);
            } else {
                User user = new User();

                String key = getSession();
                if (key != null) {
                    user.registerUser(sender, key, "lastfm_auth");
                    return "You have successfully authenticated.";
                } else {
                    return "Something went wrong during authentication.";
                }
            }

        } catch (IOException|JSONException e) {
            return "Something went wrong. " + e.getMessage();
        }
    }


    private String getSession() {
        try
        {
            String json = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=auth.getSession&api_key=" + api + "&token=" + token + "&api_sig=" + sessionSig("auth.getSession") + "&format=json");
            JSONObject obj = new JSONObject(json);
            JSONObject session = obj.getJSONObject("session");
            //System.out.println(session);
            return session.getString("key");

        } catch (IOException|JSONException e) {
            return null;
        }
    }

    private String sessionSig(String method) {
        String sig = "api_key" + api + "method" + method + "token" + token + secret;
        return getMD5(sig);
    }


    private String getMD5(String in) {
        String md5 = "";
        try
        {
            byte[] in_byte = in.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] digest = md.digest(in_byte);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++)
            {
                if ((0xff & digest[i]) < 0x10)
                {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(0xff & digest[i]));
            }
            md5 = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return md5;
    }
}