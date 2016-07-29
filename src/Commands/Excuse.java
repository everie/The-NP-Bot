package Commands;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hans on 28-07-2016.
 */
public class Excuse extends AbstractCommand {

    protected boolean handleParams(String[] params) {
        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "EXCUSE";
        String explain = "Want to make it seem like you know what you're doing even though you're clueless? " + info.getSplit();
        return explain + " Usage: " + cmd;
    }

    private String link = "http://programmingexcuses.com/";
    private ArrayList symbols = new ArrayList(Arrays.asList("?", "!", "."));

    public String getOutput() {

        try {
            URL url = new URL(link);

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            String in;
            boolean gotIt = false;

            while ((in = streamReader.readLine()) != null) {
                if (in.toLowerCase().contains("<center")) {
                    gotIt = true;
                    break;
                }
            }

            streamReader.close();

            if (gotIt) {
                String text = Jsoup.parse(in).text();
                if (!symbols.contains(text.substring(text.length()-1))) {
                    text += ".";
                }
                return text;
            } else {
                return "Something went wrong. They must've changed their layout: " + link;
            }

        } catch (IOException e) {
            return "Something went wrong. " + e.getMessage();
        }

    }
}
