package Commands;

import DataObjects.NickInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;

/**
 * Created by Hans on 28-07-2016.
 */
public class Weather extends AbstractCommand {

    private NickInfo nickInfo = new NickInfo();
    private boolean newService;

    public Weather(boolean newService) {
        this.newService = newService;
    }

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
        String cmd = info.getIdentifier() + "WEATHER";
        String explain = "Too lazy to look out the window? Let me tell you what to expect out there. " + info.getSplit();
        return explain + " Usage: " + cmd + " <IRC Nick|Location> OR " + cmd + " alone for registered irc nicks.";
    }

    public String getWunderground() {
        String api = info.getApiOpenWeaherMap();
        String ss = info.getSplit();
        String location = nickInfo.getLfmNick();
        long now = Instant.now().getEpochSecond();

        try {

            String input = toolBox.apiToString("http://autocomplete.wunderground.com/aq?query=" + URLEncoder.encode(location, "UTF-8"));
            JSONObject obj = new JSONObject(input).getJSONArray("RESULTS").getJSONObject(0);

            String newQuery = obj.getString("l");

            //System.out.println(newQuery);

            //System.out.println("http://api.wunderground.com/api/ffab84f6a1927e0d/conditions" + newQuery + ".json");

            String inputWeather = toolBox.apiToString("http://api.wunderground.com/api/ffab84f6a1927e0d/conditions" + newQuery + ".json");

            JSONObject wObj = new JSONObject(inputWeather).getJSONObject("current_observation");

            String name = wObj.getJSONObject("display_location").getString("full");
            long observation = wObj.getLong("observation_epoch");

            String weatherString = wObj.getString("weather");

            double temperature = wObj.getDouble("temp_c");
            double feelsLike = Double.parseDouble(wObj.getString("feelslike_c"));
            String humidity = wObj.getString("relative_humidity");

            //int windDeg = wObj.getInt("wind_dir");
            String dispWind = toolBox.getDirection(wObj.getInt("wind_degrees"));
            double windSpeed = Math.round((wObj.getDouble("wind_kph") / 3.6) * 100.0) / 100.0;
            //double windGusts = wObj.getDouble("wind_gust_kph");
            String dispGusts;
            try {
                double windGusts = Math.round((Double.parseDouble(wObj.getString("wind_gust_kph")) / 3.6) * 100.0) / 100.0;
                dispGusts = "(Gusts: " + windGusts + "m/s) ";
            } catch (JSONException e) {
                dispGusts = "";
            }


            int minsAgo = (int) ((now - observation) / 60);

            return "Location: " + name + " " + ss + " Weather: " + weatherString + " " + ss +
                    " Temp: " + temperature + "°C" + " (Feels like: " + feelsLike + "°C) " + ss +
                    " Humidity: " + humidity + " " + ss + " Wind: " + windSpeed + "m/s " + dispGusts + "(" + dispWind + ") " + ss +
                    " Last update: " + minsAgo + " minutes ago.";


        } catch (IOException|JSONException e) {
            if (e.getMessage().equals("JSONArray[0] not found.")) {
                return "Location \"" + location + "\" not found.";
            }
            return "Something went wrong. " + e.getMessage();
        }

    }

    public String getOutput()
    {

        if (newService) {
            return getWunderground();
        }

        String api = info.getApiOpenWeaherMap();
        String ss = info.getSplit();
        String location = nickInfo.getLfmNick();
        double windKPH = 0.0;
        String description;
        String city;
        String temperature;
        String humidity;
        String clouds;
        String windy;
        String rainy;
        String snowy;
        String update;

        try
        {
            String input = toolBox.apiToString("http://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(location, "UTF-8") + "&appid=" + api + "&units=metric");
            JSONObject obj = new JSONObject(input);

            JSONObject main = obj.getJSONObject("main");

            try
            {
                // HUMIDITY
                int hum = main.getInt("humidity");

                humidity = "Humidity: " + hum + "% " + ss + " ";

            } catch (JSONException e)
            {
                humidity = "";
            }

            try
            {
                // LOCATION
                String cName = obj.getString("name");
                JSONObject sys = obj.getJSONObject("sys");
                String cCode = sys.getString("country");

                city = "Location: " + cName + " (" + cCode + ") " + ss + " ";

            } catch (JSONException e)
            {
                city = "";
            }

            try
            {
                // CLOUDS
                clouds = "Clouds: " + obj.getJSONObject("clouds").getInt("all") + "% " + ss + " ";

            } catch (JSONException e)
            {
                clouds = "";
            }

            try
            {
                // WEATHER
                JSONArray we = obj.getJSONArray("weather");
                JSONObject desc = we.getJSONObject(0);

                String mainWeather = desc.getString("main");
                String mainDesc = desc.getString("description");

                description = "Weather: " + mainWeather + " (" + mainDesc + ") " + ss + " ";

            } catch (JSONException e)
            {
                description = "";
            }

            try
            {
                // WIND
                JSONObject wind = obj.getJSONObject("wind");
                double speed = wind.getDouble("speed");
                //int degree = wind.getInt("deg");

                windKPH = speed * 3.6;

                String direction = toolBox.getDirection(wind.getInt("deg"));

                windy = "Wind: " + speed + "m/s " + "(" + direction + ") " + ss + " ";

            } catch (JSONException e)
            {
                windy = "";
            }

            try
            {
                // TEMPERATURE
                //double tempMin = main.getDouble("temp_min");
                //double tempMax = main.getDouble("temp_max");
                double temp = main.getDouble("temp");

                double wi = Math.pow(windKPH, 0.16);
                double feel = 13.12 + 0.6215 * (temp) - 11.37 * wi + 0.3965 * (temp) * wi;
                feel = Math.round(feel*10.0)/10.0;

                //temperature = "Temperature: " + temp + "C (" + tempMin + "-" + tempMax + "), ";
                temperature = "Temperature: " + temp + "°C (Feels like: " + feel + "°C) " + ss + " ";


            } catch (JSONException e)
            {
                temperature = "";
            }

            try
            {
                // RAIN
                JSONObject rain = obj.getJSONObject("rain");
                double ramount = rain.getDouble("3h");

                rainy = "Rain volume: " + ramount + "mm " + ss + " ";

            } catch (JSONException e)
            {
                rainy = "";
            }

            try
            {
                // SNOW
                JSONObject snow = obj.getJSONObject("snow");
                double samount = snow.getDouble("3h");

                snowy = "Snow volume: " + samount + "mm " + ss + " ";

            } catch (JSONException e)
            {
                snowy = "";
            }

            try
            {
                // UPDATE
                long dt = obj.getInt("dt");
                long now = Instant.now().getEpochSecond();
                long ago = now - dt;
                int minsAgo = (int) (ago / 60.0);
                //System.out.println(minsAgo);

                update = "Last update: " + minsAgo + " minutes ago.";

            } catch (JSONException e)
            {
                update = "";
            }


            String ret = city + description + temperature + snowy + rainy + humidity + clouds + windy + update;

            return ret;

        } catch (IOException|JSONException e)
        {
            return "Something went wrong. " + e.getMessage();
        }
    }

}
