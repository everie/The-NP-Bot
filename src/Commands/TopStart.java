package Commands;

import Commands.Sub.TopPlays;
import Enums.Interval;

/**
 * Created by Hans on 26-07-2016.
 */
public class TopStart extends AbstractCommand {

    public TopStart(Interval interval) {
        this.interval = interval;
    }

    protected boolean handleParams(String[] params) {
        return true;
    }

    public String getOutput() {
        TopPlays top = new TopPlays();

        return top.getOutput(interval, channel);
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "TOP[interval]";
        String intervals = info.getSplit() + " Intervals: Week, Month, Year, All. Default: Today";
        String explain = "Displays a top list of all users currently online in a channel. " + info.getSplit();
        return explain + " Usage: " + cmd + " " + intervals;
    }
}
