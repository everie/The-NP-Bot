package Tools;

import DataObjects.BotInfo;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static org.apache.http.HttpHeaders.USER_AGENT;

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
            /*
            URL url = new URL(link);

            URLConnection con = url.openConnection();
            con.setConnectTimeout(10 * 1000);
            con.setReadTimeout(10 * 1000);

            InputStream in = con.getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

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

            */

            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10 * 1000).setSocketTimeout(10 * 1000).build();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpGet request = new HttpGet(link);

            // add request header
            request.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);

            int code = response.getStatusLine().getStatusCode();

            if (code != 200) {
                throw new IOException("Server returned " + String.valueOf(code) + ": " + response.getStatusLine().getReasonPhrase());
            }

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            Boolean found = false;

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null  && !found) {
                result.append(line);
                if (line.toLowerCase().contains("</title>")) {
                    found = true;
                }
            }

            //return result.toString();




            String page = result.toString();

            //  System.out.println(page);

            if (page.toLowerCase().contains("<title"))
            {
                String[] title = page.split("<title");
                String[] __title = title[1].split(">");
                String[] _title = __title[1].split("</title");
                String showTitle = StringEscapeUtils.unescapeHtml4(_title[0].trim());

                if (link.contains("youtube.com") || link.contains("youtu.be")) {
                    showTitle = "\u0002\u00031,0You\u0003\u000301,00\u000300,04Tube\u000F " + showTitle.substring(0, showTitle.lastIndexOf("-") - 1);
                }

                ret = toolBox.escapeName(name) + "'s link " + ss + " " + showTitle;
            }


        } catch (IOException e) {
            // Do nothing
        }

        return ret;
    }
}
