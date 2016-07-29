package DataObjects;

import Bot.Bot;

import java.util.HashSet;

/**
 * Created by Hans on 22-07-2016.
 */
public class BotInfo {

    private String ircNick;
    private String ircLogin;
    private String ircName;
    private String ircServer;

    private String ownerNick;
    private String ownerHost;

    private HashSet<String> channels;

    private boolean verbose;

    private String identifier;
    private String split;

    private String dbHost;
    private int dbPort;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    private String apiLastFM;
    private String apiTraktTV;
    private String apiGoogle;
    private String apiOpenWeaherMap;
    private String apiUrbanDictionary;
    private String apiYandex;

    private Bot bot;

    public BotInfo() {
        channels = new HashSet<>();
    }

    public void setVerbose(String verbose) {
        this.verbose = Boolean.parseBoolean(verbose);
    }

    public boolean getVerbose() {
        return verbose;
    }

    public HashSet<String> getChannels() {
        return channels;
    }

    public String getIrcNick() {
        return ircNick;
    }

    public void setIrcNick(String ircNick) {
        this.ircNick = ircNick;
    }

    public String getIrcLogin() {
        return ircLogin;
    }

    public void setIrcLogin(String ircLogin) {
        this.ircLogin = ircLogin;
    }

    public String getIrcName() {
        return ircName;
    }

    public void setIrcName(String ircName) {
        this.ircName = ircName;
    }

    public String getIrcServer() {
        return ircServer;
    }

    public void setIrcServer(String ircServer) {
        this.ircServer = ircServer;
    }

    public String getOwnerHost() {
        return ownerHost;
    }

    public void setOwnerHost(String ownerHost) {
        this.ownerHost = ownerHost;
    }

    public String getOwnerNick() {
        return ownerNick;
    }

    public void setOwnerNick(String ownerNick) {
        this.ownerNick = ownerNick;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public int getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = Integer.parseInt(dbPort);
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getApiLastFM() {
        return apiLastFM;
    }

    public void setApiLastFM(String apiLastFM) {
        this.apiLastFM = apiLastFM;
    }

    public String getApiTraktTV() {
        return apiTraktTV;
    }

    public void setApiTraktTV(String apiTraktTV) {
        this.apiTraktTV = apiTraktTV;
    }

    public String getApiGoogle() {
        return apiGoogle;
    }

    public void setApiGoogle(String apiGoogle) {
        this.apiGoogle = apiGoogle;
    }

    public String getApiOpenWeaherMap() {
        return apiOpenWeaherMap;
    }

    public void setApiOpenWeaherMap(String apiOpenWeaherMap) {
        this.apiOpenWeaherMap = apiOpenWeaherMap;
    }

    public String getApiUrbanDictionary() {
        return apiUrbanDictionary;
    }

    public void setApiUrbanDictionary(String apiUrbanDictionary) {
        this.apiUrbanDictionary = apiUrbanDictionary;
    }

    public String getApiYandex() {
        return apiYandex;
    }

    public void setApiYandex(String apiYandex) {
        this.apiYandex = apiYandex;
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }
}
