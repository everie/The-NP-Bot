package Commands;

import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Hans on 26-07-2016.
 */
public class SearchGoogle extends AbstractCommand {

    String query;

    protected boolean handleParams(String[] params) {

        if (params.length > 0) {
            query = arrayToString(params, 0);
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "G";
        String explain = "Too lazy to open your browser? LMGIFY. " + info.getSplit();
        return explain + " Usage: " + cmd + " <search query>";
    }

    public String getOutput() {
        String ret;
        String ss = info.getSplit();
        String api = info.getApiGoogle();

        try
        {
            String input = toolBox.apiToString("https://www.googleapis.com/customsearch/v1?q=" + URLEncoder.encode(query, "UTF-8") + "&key=" + api + "&cx=007391994989432444871:bwzv6v-xmru");
            JSONObject obj = new JSONObject(input);
            JSONArray items = obj.getJSONArray("items");
            JSONObject result = items.getJSONObject(0);

            String title = result.getString("title");
            String desc = result.getString("snippet");
            String link = result.getString("link");

            String formDesc = desc.replaceAll("\n", "");

            String returned = Colors.BOLD + title + Colors.NORMAL + " " + ss + " " + formDesc + " " + ss + " " + Colors.UNDERLINE + toolBox.shortURL(link);

            ret = returned;
        }
        catch(IOException|JSONException e) {
            ret = "Something went wrong. " + e.getMessage();
        }

        return ret;
    }

}
