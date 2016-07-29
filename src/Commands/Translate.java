package Commands;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Hans on 28-07-2016.
 */
public class Translate extends AbstractCommand {

    String lang;
    String text;

    protected boolean handleParams(String[] params) {

        if (params.length > 1) {
            lang = params[0];
            text = arrayToString(params, 1);
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "T";
        String explain = "Are they speaking a weird language? Join the conversation. " + info.getSplit();
        return explain + " Usage: " + cmd + " <language> <text>";
    }

    public String getOutput() {
        String api = info.getApiYandex();
        String ss = info.getSplit();

        try {
            String input = toolBox.apiToString("https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + api + "&lang=" + lang + "&text=" + URLEncoder.encode(text, "UTF-8"));
            JSONObject obj = new JSONObject(input);

            int code = obj.getInt("code");

            if (code != 200) {
                return String.valueOf(obj);
            } else {
                JSONArray trans = obj.getJSONArray("text");
                return obj.getString("lang").toUpperCase() + " " + ss + " " + String.valueOf(trans.get(0));
            }

        } catch (IOException e) {
            return "Something went wrong. Check supported languages at " + toolBox.shortURL("https://tech.yandex.com/translate/doc/dg/concepts/api-overview-docpage/#languages");
        } catch (JSONException e) {
            return "Something went wrong. " + e.getMessage();
        }

    }
}
