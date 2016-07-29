package Commands;

import Database.Seen;

/**
 * Created by Hans on 29-07-2016.
 */
public class LastSeen extends AbstractCommand {

    String nick;


    protected boolean handleParams(String[] params) {

        if (params.length > 0) {
            nick = params[0];
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "SEEN";
        String explain = "Can't remember when you've last seen that person? " + info.getSplit();
        return explain + " Usage: " + cmd + " <IRC Nick>";
    }

    public String getOutput() {
        Seen seen = new Seen();
        return seen.getLastSeen(nick, channel);
    }
}
