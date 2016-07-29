package Commands;

import Commands.Sub.Stats;
import Enums.Interval;
import DataObjects.NickInfo;

/**
 * Created by Hans on 26-07-2016.
 */
public class StatsStart extends AbstractCommand {

    NickInfo nickInfo = new NickInfo();

    public StatsStart(Interval interval) {
        this.interval = interval;
    }

    protected boolean handleParams(String[] params) {

        boolean isSelf = false;
        if (params.length > 0) {
            setNickInfo(params[0], nickInfo, "lastfm");
        } else {
            setNickInfo(sender, nickInfo, "lastfm");
            isSelf = true;
        }

        if (isSelf && !nickInfo.getIsReg()) {
            return false;
        }

        return true;
    }

    public String getOutput() {
        Stats stats = new Stats();

        return stats.getOutput(interval.getLfmInterval(), nickInfo);
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "STATS[interval]";
        String intervals = info.getSplit() + " Intervals: Week, Month, Year. Default: All";
        String explain = "Displays the stats a user in a given interval. " + info.getSplit();
        return explain + " Usage: " + cmd + " <IRC Nick|Last.fm Nick> OR " + cmd + " alone for registered irc nicks. " + intervals;
    }
}
