package Commands;

import DataObjects.NickInfo;
import DataObjects.WeatherDay;
import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hans on 28-07-2016.
 */
public class WeatherForecast extends AbstractCommand {

    private NickInfo nickInfo = new NickInfo();

    protected boolean handleParams(String[] params) {

        boolean isSelf = false;
        if (params.length > 0) {
            setNickInfo(params[0], nickInfo, "location");
        } else {
            setNickInfo(sender, nickInfo, "location");
            isSelf = true;
        }

        if (isSelf && !nickInfo.getIsReg()) {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "FORECAST";
        String explain = "A brief estimation of the weather you can expect in the coming days. " + info.getSplit();
        return explain + " Usage: " + cmd + " <IRC Nick|Location> OR " + cmd + " alone for registered irc nicks.";
    }

    public String getOutput() {
        int days = 6;
        String api = info.getApiOpenWeaherMap();
        String ss = info.getSplit();
        String location = nickInfo.getLfmNick();
        String ret = "";

        try
        {
            String input = toolBox.apiToString("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + URLEncoder.encode(location, "UTF-8") + "&units=metric&cnt=" + days + "&appid=" + api);
            JSONObject obj = new JSONObject(input);

            JSONArray list = obj.getJSONArray("list");
            JSONObject city = obj.getJSONObject("city");

            String name = city.getString("name");
            String country = city.getString("country");

            int size = list.length();
            List<WeatherDay> wd = new ArrayList<>();

            for (int i = 1; i < size; i++) {
                JSONObject day = list.getJSONObject(i);
                JSONObject temperatures = day.getJSONObject("temp");
                JSONArray weatherArray = day.getJSONArray("weather");
                JSONObject weather = weatherArray.getJSONObject(0);

                WeatherDay w = new WeatherDay();
                w.name = getDate(day.getLong("dt"));
                w.type = weather.getString("main");
                // temp
                w.morning = temperatures.getDouble("morn");
                w.day = (int)Math.round(temperatures.getDouble("day"));
                w.evening = temperatures.getDouble("eve");
                w.night = (int)Math.round(temperatures.getDouble("night"));
                // hum
                w.humidity = day.getInt("humidity");
                // clouds
                w.clouds = day.getInt("clouds");
                // wind
                w.windDeg = day.getInt("deg");
                w.windSpeed = (int)Math.round(day.getDouble("speed"));
                wd.add(w);
                //System.out.println(day);
            }

            String output = "Location: " + name + " (" + country + ") " + ss + " ";
            boolean first = true;
            for (WeatherDay w : wd) {
                String temp = "D/N: " + w.day + "°C/" + w.night + "°C";
                String line = Colors.BOLD + w.name + ": " + Colors.NORMAL + w.type + " " + ss + " " + temp + " " + ss + " W: " + w.windSpeed + "m/s (" + toolBox.getDirection(w.windDeg) + ")";
                if (first) {
                    output += line;
                    first = false;
                } else {
                    output += " " + ss + " " + line;
                }
            }
            output += ".";
            ret = output;


        } catch (IOException e) {
            ret = "Something went wrong. " + e.getMessage();
        }

        return ret;
    }

    public static String getDate(long time)
    {
        java.util.Date _date = new java.util.Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.ENGLISH);
        return sdf.format(_date);
    }
}
