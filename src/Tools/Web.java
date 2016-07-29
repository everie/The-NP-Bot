package Tools;

import DataObjects.BotInfo;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Hans on 29-07-2016.
 */
public class Web {
    Toolbox toolBox = new Toolbox();
    BotInfo info = Info.getInfo();

    public String getTitle(String link, String name) {

        String ss = info.getSplit();
        String ret = "";

        try
        {
            URL url = new URL(link);

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();

            Boolean found = false;
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null && !found)
            {
                responseStrBuilder.append(inputStr);
                if (inputStr.toLowerCase().contains("</title>")) {
                    found = true;
                }
            }

            String input = responseStrBuilder.toString();

            streamReader.close();


            String page = input;

            //  System.out.println(page);

            if (page.toLowerCase().contains("<title"))
            {
                String[] title = page.split("<title");
                String[] __title = title[1].split(">");
                String[] _title = __title[1].split("</title");
                String showTitle = _title[0].trim();

                ret = toolBox.escapeName(name) + "'s link " + ss + " " + StringEscapeUtils.unescapeHtml4(showTitle);
            }


        } catch (IOException e) {
            // Do nothing
        }

        return ret;
    }
}
