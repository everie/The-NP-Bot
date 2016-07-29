package Commands;

import Database.Seen;

/**
 * Created by Hans on 29-07-2016.
 */
public class LastSeen24 extends AbstractCommand {

    protected boolean handleParams(String[] params) {
        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "SEEN24";
        String explain = "Displays the amount of active users in the last 24 hours. " + info.getSplit();
        return explain + " Usage: " + cmd;
    }

    public String getOutput() {
        Seen seen = new Seen();
        return seen.getTotalSeen(channel);
    }
}
