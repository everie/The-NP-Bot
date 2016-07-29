package Commands;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Hans on 28-07-2016.
 */
public class UrbanDictionary extends AbstractCommand {

    String term;

    protected boolean handleParams(String[] params) {

        if (params.length > 0) {
            term = arrayToString(params, 0);
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "UD";
        String explain = "Lets you translate weird acronyms into English via UrbanDictionary. " + info.getSplit();
        return explain + " Usage: " + cmd + " <word> OR " + cmd + " <index> <word>";
    }

    public String getOutput() {

        int displayLength = 256;
        String ss = info.getSplit();

        try
        {
            int index = 0;
            try {
                index = Integer.parseInt(term.split(" ")[0]);
                term = term.substring(String.valueOf(index + " ").length());
                //System.out.println(index + ", " + term);
            } catch (NumberFormatException nfe) {
                // do nothing
            }

            String input = getData("https://mashape-community-urban-dictionary.p.mashape.com/define?term=" + URLEncoder.encode(term, "UTF-8"));
            JSONObject obj = new JSONObject(input);
            JSONArray list = obj.getJSONArray("list");

            if (list.length() < 1) {
                return "Definition of \"" + term + "\" wasn't found.";
            }

            if (index > list.length()) {
                index = list.length();
            }

            int realIndex = 0;
            if (index != 0) {
                realIndex = index - 1;
            }

            JSONObject item = list.getJSONObject(realIndex);

            String pre = "Definition of \"" + term + "\" " + ss + " " + (realIndex + 1) + "/" + list.length() + " • ";
            //String definition = item.getString("definition") + " • " + item.getString("example");
            String definition = item.getString("definition");
            definition = definition.replace("\n", " ").replace("\r", " ");
            if (definition.length() > displayLength) {
                definition = definition.substring(0, displayLength) + "... " + toolBox.shortURL(item.getString("permalink"));
            }

            return pre + definition;

        } catch (IOException|JSONException e) {
            return "Something went wrong. " + e.getMessage();
        }
    }

    public String getData(String link) {

        String api = info.getApiUrbanDictionary();

        try
        {
            URL url = new URL(link);
            HttpsURLConnection con;

            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Mashape-Key", api);

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
            {
                responseStrBuilder.append(inputStr);
            }

            streamReader.close();

            return responseStrBuilder.toString();

        } catch (IOException e) {
            return "Something went wrong. " + e.getMessage();
        }
    }
}
