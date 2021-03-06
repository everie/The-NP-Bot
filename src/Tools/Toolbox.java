package Tools;

import DataObjects.BotInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Created by Hans on 23-07-2016.
 */
public class Toolbox {

    BotInfo info = Info.getInfo();

    public String apiToString(String in) throws IOException {
        /*
        URL url = new URL(in);

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

        //System.out.println(streamReader);

        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
        {
            responseStrBuilder.append(inputStr);
        }

        String input = responseStrBuilder.toString();

        streamReader.close();

        return input;
        */

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(in);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        int code = response.getStatusLine().getStatusCode();

        if (code != 200) {
            throw new IOException("Server returned " + String.valueOf(code) + ": " + response.getStatusLine().getReasonPhrase());
        }

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();

    }

    public String shortURL(String url) {

        String ret;

        try
        {
            String input = apiToString("http://lt3.in/s/?json&url=" + URLEncoder.encode(url, "UTF-8"));
            JSONObject obj = new JSONObject(input);
            ret = obj.getString("link");

        }catch(IOException e) {
            ret = "Something went wrong. " + e.getMessage();
        }
        catch(JSONException e) {
            ret = "Something went wrong. " + e.getMessage();
        }

        return ret;
    }

    public String shortSpotURL(String url) {

        String ret;

        try
        {
            String input = apiToString("http://lt3.in/spot/?url=" + url + "&json");
            JSONObject obj = new JSONObject(input);
            ret = obj.getString("link");

        }catch(IOException e) {
            ret = "Something went wrong. " + e.getMessage();
        }
        catch(JSONException e) {
            ret = "Something went wrong. " + e.getMessage();
        }

        return ret;
    }

    public String showTime(int dur) {
        String ret;
        if (dur > 3600000) {
            ret = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(dur),
                    TimeUnit.MILLISECONDS.toMinutes(dur) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(dur)),
                    TimeUnit.MILLISECONDS.toSeconds(dur) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur)));
        } else {
            ret = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(dur),
                    TimeUnit.MILLISECONDS.toSeconds(dur) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur))
            );
        }
        return ret;
    }

    public String showDuration(long dur) {
        String ret;

        long hour = 3600000;
        long day = hour * 24;

        if (dur >= day) {
            ret = String.format("%dd, %dh, %dm, %ds",
                    TimeUnit.MILLISECONDS.toDays(dur),
                    TimeUnit.MILLISECONDS.toHours(dur) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(dur)),
                    TimeUnit.MILLISECONDS.toMinutes(dur) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(dur)),
                    TimeUnit.MILLISECONDS.toSeconds(dur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur))
            );
        } else if (dur > hour && dur < day) {
            ret = String.format("%dh, %dm, %ds",
                    TimeUnit.MILLISECONDS.toHours(dur),
                    TimeUnit.MILLISECONDS.toMinutes(dur) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(dur)),
                    TimeUnit.MILLISECONDS.toSeconds(dur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur))
            );
        } else {
            ret = String.format("%dm, %ds",
                    TimeUnit.MILLISECONDS.toMinutes(dur),
                    TimeUnit.MILLISECONDS.toSeconds(dur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur))
            );
        }
        return ret;
    }




    public String escapeName(String name) {
        int len = name.length();
        String n1 = name.substring(0,1);
        String esc = "\u200B";
        String n2 = name.substring(1,len);
        return n1 + esc + n2;
    }

    public String boldEscapeName(String name) {
        int len = name.length();
        String n1 = name.substring(0,1);
        String esc = "\u200B";
        String n2 = name.substring(1,len);
        return Colors.BOLD + n1 + esc + n2 + Colors.NORMAL;
    }

    public long getFromInterval(int days) {
        if (days == 0) {
            return 0;
        } else {
            return Instant.now().getEpochSecond() - (days * 24 * 60 * 60);
        }
    }

    public String getDateFromTS(long ts)
    {
        java.util.Date _date = new java.util.Date(ts * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        return sdf.format(_date);
    }

    public HashMap<String, String> getCurrentTrack(String nick) {

        String api = info.getApiLastFM();
        HashMap<String, String> returnMap = new HashMap<>();

        try
        {
            String input = apiToString("http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&user=" + nick + "&api_key=" + api + "&format=json&limit=2");
            JSONObject obj = new JSONObject(input);
            JSONObject rec = obj.getJSONObject("recenttracks");
            JSONArray trackArray = rec.getJSONArray("track");
            JSONObject cur = trackArray.getJSONObject(0);

            // Testing if this is currently playing track
            JSONObject attr = cur.getJSONObject("@attr");

            JSONObject art = cur.getJSONObject("artist");
            String artist = art.getString("#text");
            String track = cur.getString("name");

            returnMap.put("artist", artist);
            returnMap.put("track", track);

            return returnMap;

        }
        catch(IOException|JSONException e) {
            return null;
        }

    }

    public String getDirection(int d)
    {
        // 337.5-22.5 ↓, 22.5-67.5 ↙, 67.5-112.5 ←, 112.5-157.5 ↖, 157.5-202.5 ↑, 202.5-247.5 ↗, 247.5-292.5 →, 292.5-337.5 ↘
        // ←↑→↓↔↕↖↗↘↙
        if (d > 337.5 && d <= 360 || d >= 0 && d < 22.5)
        {
            return "↓";
        } else if (d > 22.5 && d < 67.5)
        {
            return "↙";
        } else if (d > 67.5 && d < 112.5)
        {
            return "←";
        } else if (d > 112.5 && d < 157.5)
        {
            return "↖";
        } else if (d > 157.5 && d < 202.5)
        {
            return "↑";
        } else if (d > 202.5 && d < 247.5)
        {
            return "↗";
        } else if (d > 247.5 && d < 292.5)
        {
            return "→";
        } else if (d > 292.5 && d < 337.5)
        {
            return "↘";
        } else
        {
            return "n/a";
        }
    }

    public String getMD5(String in) {
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
