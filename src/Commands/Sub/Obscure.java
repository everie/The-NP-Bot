package Commands.Sub;

import DataObjects.ArtistListeners;
import DataObjects.ArtistObscure;
import DataObjects.BotInfo;
import DataObjects.NickInfo;
import Tools.Info;
import Tools.Toolbox;
import org.jibble.pircbot.Colors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.*;

/**
 * Created by Hans on 15-08-2016.
 */
public class Obscure {

    Toolbox toolBox = new Toolbox();
    BotInfo info = Info.getInfo();
    private ArrayList<ArtistObscure> artistList = new ArrayList<>();

    private int limit = 20;
    private String alFilename = "ArtistListeners.ser";

    public String getOutput(String interval, NickInfo nickInfo) {

        String ss = info.getSplit();
        String api = info.getApiLastFM();
        String nick = nickInfo.getLfmNick();

        String description;

        switch (interval) {
            case "7day":
                description = "Last week";
                break;

            case "1month":
                description = "Last month";
                break;

            case "12month":
                description = "Last year";
                break;

            default:
                description = "Overall";
                break;
        }


        long expiryDate = Instant.now().getEpochSecond() + (14 * 24 * 60 * 60);
        ArtistListeners al = loadObject();
        if (al == null) {
            al = new ArtistListeners();
        }

        try {
            String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user=" + nick + "&api_key=" + api + "&format=json&limit=" + limit + "&period=" + interval);
            JSONObject obj = new JSONObject(input);

            JSONObject artists = obj.getJSONObject("topartists");
            JSONArray artist = artists.getJSONArray("artist");

            for (int i = 0; i < artist.length(); i++) {
                JSONObject current = artist.getJSONObject(i);

                String currentArtist = current.getString("name");
                int listeners;
                if (al.getMap().containsKey(currentArtist)) {
                    artistList.add(al.getMap().get(currentArtist));
                } else {
                    try {
                        String inputArtist = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + URLEncoder.encode(currentArtist, "UTF-8") + "&api_key=" + api + "&format=json");

                        JSONObject objArtist = new JSONObject(inputArtist);
                        listeners = objArtist.getJSONObject("artist").getJSONObject("stats").getInt("listeners");

                        ArtistObscure ao = new ArtistObscure(currentArtist, listeners, expiryDate);

                        al.getMap().put(currentArtist, ao);
                        artistList.add(ao);
                    } catch (JSONException e) {
                        System.out.println("Failed artists: " + currentArtist);
                    }
                }
            }

            saveObject(al);

            Collections.sort(artistList, Comparator.reverseOrder());

            double points = 0;
            //double points2 = 0;
            String dispObscure = "";
            String leastObscureArtist = "";
            int leastObscureListeners = 0;
            int listSize = artistList.size();

            for (int i = 0; i < listSize; i++) {
                ArtistObscure ao = artistList.get(i);
                if (i < 3) {
                    if (i > 0) {
                        dispObscure += " " + ss + " ";
                    }
                    dispObscure += ao.getArtist() + " (" + ao.getListeners() + " listeners)";
                }
                if (i == listSize - 1) {
                    leastObscureArtist = ao.getArtist();
                    leastObscureListeners = ao.getListeners();
                }
                //System.out.println(ao.getListeners());
                //System.out.println(getPoints(ao.getListeners(), listSize));
                //System.out.println(getCalculatedPoints(ao.getListeners(), listSize));
                //points2 += getCalculatedPoints(ao.getListeners(), listSize);
                points += getCalculatedPoints(ao.getListeners(), listSize);
            }

            String dispPct;
            double totalPoints = Math.round(points * 10.0) / 10.0;

            if (totalPoints == Math.round(totalPoints)) {
                dispPct = (int)totalPoints + "%";
            } else {
                dispPct = totalPoints + "%";
            }

            //System.out.println(points2 + " " + totalPoints);

            return description + " " + displayNick(nickInfo) + " has been " + Colors.BOLD + dispPct + Colors.NORMAL + " hipster " + ss + " Most obscure: " + dispObscure +
                    " " + ss + " Least obscure: " + leastObscureArtist + " (" + leastObscureListeners + " listeners)";


        } catch (IOException |JSONException e) {
            return "Something went wrong. " + e.getMessage();
        }

    }

    private String displayNick(NickInfo ni) {
        if (ni.getIsReg()) {
            return Colors.BOLD + toolBox.escapeName(ni.getIrcNick()) + Colors.NORMAL;
        } else {
            return ni.getLfmNick();
        }
    }

    private double getPoints(int listeners, int size) {
        //double pointsPrArtist = 100.0 / (double)size;
        if (listeners <= 500) {
            return 5;
        } else if (listeners > 500 & listeners <= 2000) {
            return 4.5;
        } else if (listeners > 2000 & listeners <= 5000) {
            return 4;
        } else if (listeners > 5000 & listeners <= 10000) {
            return 3.5;
        } else if (listeners > 10000 & listeners <= 20000) {
            return 3;
        } else if (listeners > 20000 & listeners <= 50000) {
            return 2.5;
        } else if (listeners > 50000 & listeners <= 100000) {
            return 2;
        } else if (listeners > 100000 & listeners <= 500000) {
            return 1.5;
        } else if (listeners > 500000 & listeners <= 1000000) {
            return 1;
        } else {
            return 0;
        }
    }

    private double getCalculatedPoints(int listeners, int size) {
        double pointsPrArtist = 100.0 / (double)size;
        //double points = 6.5707 * (1.0 / Math.pow((double)listeners, 0.30294));
        //double points = 1.00231 * (1.0 / Math.pow(Math.E, 0.000004607470000 * (double)listeners));
        double points = 1.00346 * (1.0 / Math.pow(Math.E, 0.00000691121 * (double)listeners));
        //double points = -0.130248 * Math.log10(Math.pow(9.26097,-7) * (double)listeners);
        //System.out.println(points);
        if (points > 1) {
            points = 1.0;
        }
        return points * pointsPrArtist;
    }


    private void saveObject(ArtistListeners al) {
        try {
            FileOutputStream fileOut = new FileOutputStream(alFilename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(al);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArtistListeners loadObject() {
        try {
            FileInputStream fileIn = new FileInputStream(alFilename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArtistListeners al = (ArtistListeners) in.readObject();
            in.close();
            fileIn.close();
            return cleanObject(al);
        } catch (IOException|ClassNotFoundException e) {
            return null;
        }
    }

    private ArtistListeners cleanObject(ArtistListeners al) {
        long now = Instant.now().getEpochSecond();
        al.getMap().entrySet().removeIf(e -> e.getValue().getExpire() < now);
        return al;
    }

}
