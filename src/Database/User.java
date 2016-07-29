package Database;

import DataObjects.UserScore;
import Tools.Toolbox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans on 22-07-2016.
 */
public class User {

    private DatabaseConnector dbc = DatabaseConnector.getInstance();

    private String getUserInfo(String nick, String type) {

        try
        {
            Connection con = dbc.getConnection();
            PreparedStatement state;
            ResultSet rs;

            String getUser = "SELECT " + type + " FROM " + dbc.getTableUsers() + " WHERE nick LIKE ?";

            state = con.prepareStatement(getUser);
            state.setString(1, changeWildCard(nick));
            rs = state.executeQuery();

            String returnNick;

            if (rs.next())
            {
                returnNick = rs.getString(type);
            } else {
                returnNick = null;
            }

            state.close();
            rs.close();
            return returnNick;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String changeWildCard(String input) {
        return input.replaceAll("\\*", "%");
    }

    public String getUser(String nick, String type)
    {
        User user = new User();
        String n = user.getUserInfo(nick, type);
        if (n == null)
        {
            return nick;
        } else
        {
            return n;
        }
    }

    public boolean isUser(String nick, String type)
    {
        User user = new User();
        String n = user.getUserInfo(nick, type);
        if (n == null)
        {
            return false;
        } else
        {
            return true;
        }
    }

    public List<UserScore> getAllLastFM(List<String> online) {
        List<UserScore> list = new ArrayList<>();

        try
        {
            Connection con = dbc.getConnection();
            PreparedStatement state;
            ResultSet rs;

            String getUser = "SELECT nick,lastfm FROM " + dbc.getTableUsers() + " WHERE lastfm IS NOT NULL";

            state = con.prepareStatement(getUser);
            rs = state.executeQuery();

            List<String> users = new ArrayList<>();


            while (rs.next()) {
                String nick = rs.getString("nick");
                String lastfm = rs.getString("lastfm");

                if (!users.contains(lastfm) && online.contains(nick)) {
                    users.add(lastfm);
                    list.add(new UserScore(nick, lastfm));
                }
            }

            state.close();
            rs.close();

            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public String registerUser(String sender, String input, String type) {

        Toolbox toolBox = new Toolbox();

        try
        {
            Connection con = dbc.getConnection();
            PreparedStatement state;

            String dispType;
            String dbType;

            switch (type) {
                case "lastfm":
                    dispType = "Last.fm account";
                    dbType = "(?, ?, null, null)";
                    break;
                case "trakt":
                    dispType = "Trakt.tv account";
                    dbType = "(?, null, ?, null)";
                    break;
                case "location":
                    dispType = "Location";
                    dbType = "(?, null, null, ?)";
                    break;

                default:
                    dispType = "Last.fm account";
                    dbType = "(?, ?, null, null)";
                    break;
            }

            String updateUser = "INSERT INTO " + dbc.getTableUsers() + " (nick, lastfm, trakt, location) VALUES " + dbType +
                    " ON DUPLICATE KEY UPDATE " + type + " = VALUES(" + type + ")";

            state = con.prepareStatement(updateUser);
            state.setString(1, sender);
            state.setString(2, input);
            state.executeUpdate();

            state.close();

            return dispType + " for " + toolBox.boldEscapeName(sender) + " is set to: " + input;

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }
}
