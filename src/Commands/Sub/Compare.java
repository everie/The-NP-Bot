package Commands.Sub;

import DataObjects.ArtistScore;
import DataObjects.BotInfo;
import DataObjects.NickInfo;
import Tools.Info;
import Tools.Toolbox;
import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by Hans on 25-07-2016.
 */
public class Compare {

    private int calcSize = 20;
    private int dispSize = 12;

    private int limit = 1000;

    private Toolbox toolBox = new Toolbox();
    BotInfo info = Info.getInfo();

    public String getOutput(String interval, NickInfo nickInfo1, NickInfo nickInfo2) {

        String ss = info.getSplit();
        String api = info.getApiLastFM();
        String nick1 = nickInfo1.getLfmNick();
        String nick2 = nickInfo2.getLfmNick();

        int commonArtists = 0;
        try
        {

            String input1 = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=library.getartists&api_key=" + api + "&user=" + nick1 + "&format=json&limit=" + limit + "&period=" + interval);
            String input2 = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=library.getartists&api_key=" + api + "&user=" + nick2 + "&format=json&limit=" + limit + "&period=" + interval);
            JSONObject obj1 = new JSONObject(input1);
            JSONObject obj2 = new JSONObject(input2);
            JSONArray arr1 = obj1.getJSONObject("artists").getJSONArray("artist");
            JSONArray arr2 = obj2.getJSONObject("artists").getJSONArray("artist");

            Map<String, Double> m1 = new HashMap<>(getMap2(arr1));
            Map<String, Double> m2 = new HashMap<>(getMap2(arr2));

            List<ArtistScore> commons = new ArrayList<>();

            for (Map.Entry<String, Double> entry : m1.entrySet())
            {
                if (m2.containsKey(entry.getKey())) {
                    commonArtists++;

                    String e = entry.getKey();

                    double p1 = entry.getValue();
                    double p2 = m2.get(e);
                    double diff = Math.abs(p1-p2);
                    //System.out.println((1/((p1+p2)*(p1-p2))) + " -- " + Math.abs(p1-p2));



                    if (diff < 0.4)
                    {
                        double p3 = (p1 + p2) / 2.0;
                        commons.add(new ArtistScore(e, p3));

                        //System.out.println(e + " " + p1 + " " + p2 + " " + p3);
                    }
                }
            }

            Collections.sort(commons);

            double points = 0;
            int iterator = 0;

            double ps = 100.0/(double)calcSize;

            String dispArtists = "";

            for (ArtistScore as : commons) {
                if (iterator < calcSize) {
                    if (iterator < dispSize)
                    {
                        if (iterator > 0) {
                            dispArtists += ", ";
                        }
                        dispArtists += as.getArtist() + " (" + (int)Math.round(as.getScore() * 100) + "%)";
                    }

                    Double ep = as.getScore();
                    points += ps * ep;
                }

                iterator++;
            }

            points = Math.round(points*10.0)/10.0;
            double common = Math.round(((double)commonArtists * 10.0 / (double)limit)*100.0) / 10.0;
            String love = "";
            if (points >= 60) {
                love = Colors.BOLD + "<3 " + points + "%" + Colors.NORMAL;
            } else if (points >= 33 && points < 60) {
                love = "<3 " + points + "%";
            } else if (points < 33) {
                love = "</3 " + points + "%";
            }

            String commonPart;

            if (commonArtists == 0) {
                commonPart = " Nothing in common.";
            } else {
                commonPart = " Commons: " + common + "% " + ss + " " + dispArtists;
            }

            return displayNick(nickInfo1) + " ↔ " + displayNick(nickInfo2) + " " + ss + " " + love + " " + ss + commonPart;

        }catch (IOException|JSONException e) {
            return "Something went wrong. " + e.getMessage();
        }
    }



    private Map<String, Double> getMap(JSONArray in) {
        Map<String, Double> list = new HashMap<>();
        int size = in.length();

        for (int x = 0; x < size; x++) {
            JSONObject row = in.getJSONObject(x);
            String name = row.getString("name");

            Double score;
            if (x < calcSize) {
                score = 1.0;
            } else
            {
                score = ((double) size - (double) x) / (double) size;
                if (score < 0.6) {
                    score = score * 0.5;
                }
            }

            list.put(name, score);
        }

        return list;
    }

    private Map<String, Double> getMap2(JSONArray in) {
        Map<String, Double> list = new HashMap<>();
        int size = in.length();

        int top = 0;
        boolean topSet = false;

        for (int x = 0; x < size; x++) {
            JSONObject row = in.getJSONObject(x);
            String name = row.getString("name");
            int pc = row.getInt("playcount");

            Double score;
            if (x < calcSize) {
                score = 1.0;
            } else
            {
                if (!topSet) {
                    top = pc;
                    topSet = true;
                }
                score = (double)pc / (double)top;
            }

            list.put(name, score);
        }

        return list;
    }

    protected String displayNick(NickInfo ni) {
        if (ni.getIsReg()) {
            return Colors.BOLD + toolBox.escapeName(ni.getIrcNick()) + Colors.NORMAL;
        } else {
            return ni.getLfmNick();
        }
    }

}
