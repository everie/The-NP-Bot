package Commands;

import Database.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        String cmd = info.getIdentifier() + "AUTH";
        String explain = "Lets you authenticate your last.fm account with the bot. " + info.getSplit();
        return explain + " Usage: /msg " + info.getIrcNick() + " " + cmd;
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
        return toolBox.getMD5(sig);
    }



}