package Commands;

import DataObjects.BotInfo;
import Enums.Interval;
import DataObjects.NickInfo;
import Database.User;
import Tools.Info;
import Tools.Toolbox;
import org.jibble.pircbot.Colors;

/**
 * Created by Hans on 23-07-2016.
 */
public abstract class AbstractCommand {

    protected Toolbox toolBox = new Toolbox();
    protected BotInfo info = Info.getInfo();

    protected String[] params;
    protected String sender;
    protected String channel;
    protected String hostname;

    protected Interval interval;

    public boolean instantiate(String[] params, String sender, String channel, String hostname) {
        this.params = params;
        this.sender = sender;
        this.channel = channel;
        this.hostname = hostname;

        boolean success = handleParams(params);

        return success;
    }

    protected void setNickInfo(String nick, NickInfo ni, String type) {
        User user = new User();
        //String type = "lastfm";

        if (user.isUser(nick, type)) {
            ni.setisReg(true);
            ni.setIrcNick(nick);
            ni.setLfmNick(user.getUser(nick, type));
        } else {
            ni.setLfmNick(nick);
            ni.setisReg(false);
        }
    }

    protected String displayNick(NickInfo ni) {
        if (ni.getIsReg()) {
            return Colors.BOLD + toolBox.escapeName(ni.getIrcNick()) + Colors.NORMAL;
        } else {
            return ni.getLfmNick();
        }
    }

    public abstract String getOutput();
    public abstract String getHelp();
    protected abstract boolean handleParams(String[] params);

    public Interval getInterval() {
        return this.interval;
    }

    protected String arrayToString(String[] params, int from) {
        String output = "";

        if (params.length < 2) {
            return params[0];
        }

        for (int i = from; i < params.length; i++) {
            if (i > from) {
                output += " ";
            }
            output += params[i];
        }

        return output;
    }
}
