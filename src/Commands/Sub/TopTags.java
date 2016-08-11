package Commands.Sub;

import DataObjects.BotInfo;
import DataObjects.NickInfo;
import DataObjects.TagScore;
import Tools.Info;
import Tools.Toolbox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.*;

/**
 * Created by Hans on 29-07-2016.
 */
public class TopTags {
    Toolbox toolBox = new Toolbox();
    BotInfo info = Info.getInfo();

    public String getOutput(String interval, NickInfo nickInfo)
    {
        int limit = 50;
        String dir = "artist_tags";
        String api = info.getApiLastFM();
        String nick = nickInfo.getLfmNick();

        String ret = "";

        File fDir = new File(dir);
        File aFile;
        Scanner in;
        long now = Instant.now().getEpochSecond();
        long expiryDate = Instant.now().getEpochSecond() + (90 * 24 * 60 * 60);
        //long expiryDate = now;

        String firstLine = "expire," + expiryDate;

        Set<String> ignore = new HashSet<>(Arrays.asList("SEEN LIVE"));

        Map<String, List<TagScore>> artistTags = new HashMap<>();

        if (!fDir.exists()) {
            fDir.mkdir();
        }

        try
        {
            String input = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=library.getartists&api_key=" + api + "&user=" + nick + "&format=json&limit=" + limit + "&period=" + interval);

            JSONObject obj = new JSONObject(input);
            JSONArray arr = obj.getJSONObject("artists").getJSONArray("artist");

            int size = arr.length();

            if (size < 1) {
                return "No artists found for user " + nick + ", try scrobbling something.";
            }

            //int size = 10;
            Map<String, Integer> artists = new HashMap<>();
            List<String> newArtists = new ArrayList<>();

            for (int x = 0; x < size; x++)
            {
                Boolean add = false;

                JSONObject row = arr.getJSONObject(x);
                String name = row.getString("name");
                String encName = URLEncoder.encode(name, "UTF-8");
                aFile = new File(dir + File.separator + encName + ".txt");
                if (aFile.exists()) {
                    in = new Scanner(aFile);
                    String[] time = in.nextLine().split(",");
                    try
                    {
                        long expire = Integer.parseInt(time[1]);
                        if (now > expire)
                        {
                            add = true;
                        } else {
                            // FILE EXISTS, ADDING TO MAP!
                            List<TagScore> tags = new ArrayList<>();

                            while (in.hasNextLine()) {
                                String[] tag = in.nextLine().split(",");
                                tags.add(new TagScore(tag[0], countScore(Integer.parseInt(tag[1]), x, size)));
                            }

                            artistTags.put(name, tags);
                        }
                    } catch (NumberFormatException e) {
                        add = true;
                    }
                } else {
                    add = true;
                }

                if (add) {
                    newArtists.add(name);
                }

                artists.put(name, x);
            }

            for (String a : newArtists) {

                List<TagScore> tags = new ArrayList<>();

                String encName = URLEncoder.encode(a);
                aFile = new File(dir + File.separator + encName + ".txt");

                String input2 = toolBox.apiToString("http://ws.audioscrobbler.com/2.0/?method=artist.gettoptags&artist=" + encName + "&api_key=" + api + "&format=json");

                JSONObject obj2 = new JSONObject(input2);
                JSONArray arr2 = obj2.getJSONObject("toptags").getJSONArray("tag");

                int len = arr2.length();

                FileOutputStream fos = new FileOutputStream(aFile);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

                bw.write(firstLine);
                bw.newLine();

                for (int y = 0; y < len; y++)
                {

                    JSONObject o = arr2.getJSONObject(y);

                    String tag = o.getString("name").toUpperCase();
                    int count = o.getInt("count");
                    int pos = artists.get(a);

                    tags.add(new TagScore(tag, countScore(count, pos, size)));
                    bw.write(tag + "," + count);
                    bw.newLine();

                }
                bw.close();

                artistTags.put(a, tags);

            }

            Map<String,TagScore> tagScores = new HashMap<>();
            int total = 0;

            for (Map.Entry<String,List<TagScore>> entry : artistTags.entrySet())
            {

                for(TagScore ts : entry.getValue()) {
                    String t = ts.getTag();
                    int s = ts.getScore();
                    if (!ignore.contains(t))
                    {
                        if (tagScores.containsKey(t))
                        {
                            ts.incScore(tagScores.get(t).getScore());
                            tagScores.put(t, ts);
                        } else
                        {
                            tagScores.put(t, new TagScore(t, s));
                        }
                    }
                    total += s;
                }

            }

            List<TagScore> scores = new ArrayList<>(tagScores.values());
            Collections.sort(scores);

            int scoreDisp = 14;
            String dispTags = "";

            for (int z = 0; z < scoreDisp; z++) {
                String t = scores.get(z).getTag().toLowerCase();
                int s = scores.get(z).getScore();
                double pct = (Math.round((double)s * 1000.0 / (double)total)/10.0);
                if (z == 0) {
                    dispTags += t + " (" + pct + "%)";
                } else {
                    dispTags += ", " + t + " (" + pct + "%)";
                }
            }

            ret = dispTags + ".";

        } catch (IOException e) {
            ret = "Something went wrong. " + e.getMessage();
        }

        return ret;
    }

    private int countScore(int score, int pos, int size) {
        double bonus;
        if (score >= 50) {
            bonus = ((double)(size - pos) / (double)size) * 2;
        } else if (score < 50 && score > 30) {
            bonus = 0.75;
        } else {
            bonus = 0;
        }
        return (int)Math.round(score * bonus);
    }
}
