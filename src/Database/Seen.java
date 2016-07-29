package Database;

import DataObjects.BotInfo;
import DataObjects.SeenRow;
import Tools.Info;
import Tools.Toolbox;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Created by Hans on 29-07-2016.
 */
public class Seen {

    private Toolbox toolBox = new Toolbox();
    private BotInfo info = Info.getInfo();
    private DatabaseConnector dbc = DatabaseConnector.getInstance();

    public void setLastSeen(String nick, String comment, String channel) {

        try
        {
            Connection con = dbc.getConnection();
            PreparedStatement state;

            long seen = Instant.now().getEpochSecond();

            String updateUser = "INSERT INTO " + dbc.getTableSeen() + " (nick, last_seen, comment, channel) VALUES (?, ?, ?, ?)" +
                    "ON DUPLICATE KEY UPDATE last_seen = VALUES(last_seen), comment = VALUES(comment)";

            state = con.prepareStatement(updateUser);
            state.setString(1, nick);
            state.setLong(2, seen);
            state.setString(3, comment);
            state.setString(4, channel);
            state.executeUpdate();

            state.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLastSeen(String nick, String channel) {

        int maxComment = 100;
        String ss = info.getSplit();

        try
        {
            Connection con = dbc.getConnection();
            PreparedStatement state;
            ResultSet rs;

            String getUser = "SELECT * FROM " + dbc.getTableSeen() + " WHERE nick LIKE ? AND channel = ?";

            state = con.prepareStatement(getUser);
            state.setString(1, changeWildCard(nick));
            state.setString(2, channel);
            rs = state.executeQuery();

            ArrayList<SeenRow> seenList = new ArrayList<>();

            while (rs.next()) {
                seenList.add(new SeenRow(rs.getString("nick"), rs.getLong("last_seen"), rs.getString("comment")));
            }

            state.close();

            int size = seenList.size();

            if (size > 0) {
                if (size == 1) {
                    SeenRow sr = seenList.get(0);
                    String comment = sr.getComment();

                    if (comment.length() > maxComment) {
                        comment = comment.substring(0, maxComment) + "...";
                    }

                    long now = Instant.now().getEpochSecond();
                    long dur = (now - sr.getSeen()) * 1000;

                    return toolBox.escapeName(sr.getNick()) + " was last seen " + toolBox.getDateFromTS(sr.getSeen()) + " (" + toolBox.showDuration(dur) + " ago) saying: " + comment;
                } else {
                    int max = 5;

                    if (size < max) {
                        max = size;
                    }

                    String output = "";

                    for (int i = 0; i < max; i++) {
                        SeenRow sr = seenList.get(i);
                        if (i > 0) {
                            output += ", ";
                        }
                        output += toolBox.escapeName(sr.getNick());
                    }

                    output = "Several nicks match your request " + ss + " " + output;

                    if (size > max) {
                        output += " (and " + (size - max) + " more)";
                    }

                    return output;
                }
            } else {
                return "I haven't seen " + toolBox.escapeName(nick) + " before.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String changeWildCard(String input) {
        return input.replaceAll("\\*", "%");
    }

    public String getTotalSeen(String channel) {
        try
        {
            Connection con = dbc.getConnection();
            PreparedStatement state;
            ResultSet rs;

            long yesterday = Instant.now().getEpochSecond() - 86400;

            String getUser = "SELECT COUNT(nick) AS users FROM " + dbc.getTableSeen() + " WHERE last_seen > ? AND channel = ?";

            state = con.prepareStatement(getUser);
            state.setLong(1, yesterday);
            state.setString(2, channel);
            rs = state.executeQuery();

            int seen = 0;
            String name = "";

            while (rs.next())
            {
                seen = rs.getInt("users");
                System.out.println(seen);
            }

            state.close();

            return "I've seen " + seen + " active nicks last 24 hours.";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }
}
