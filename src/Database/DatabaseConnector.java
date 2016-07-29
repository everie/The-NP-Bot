package Database;

import DataObjects.BotInfo;
import Tools.Info;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Hans on 22-07-2016.
 */
public class DatabaseConnector {

    private static DatabaseConnector instance;
    private static BotInfo info = Info.getInfo();

    private Connection connection;

    private String host;
    private String user;
    private String pass;

    private static String tableUsers = "ircUsers";
    private static String tableSeen = "ircSeen";
    private static String extras = "?characterEncoding=UTF-8&useSSL=false";

    private DatabaseConnector(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
    }

    public static DatabaseConnector getInstance() {
        String h = "jdbc:mysql://" + info.getDbHost() + ":" + info.getDbPort() + "/" + info.getDbName() + extras;
        String u = info.getDbUser();
        String p = info.getDbPassword();

        instance = new DatabaseConnector(h, u, p);

        return instance;
    }

    public Connection connect() throws SQLException
    {
        connection = DriverManager.getConnection(host, user, pass);

        return connection;
    }

    public Connection getConnection() throws SQLException
    {
        if(connection!= null && !connection.isClosed())
        {
            return connection;
        }
        else
        {
            connect();
            return connection;
        }
    }

    public static String getTableUsers() {
        return tableUsers;
    }

    public static String getTableSeen() {
        return tableSeen;
    }
}
