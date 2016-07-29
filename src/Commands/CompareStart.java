package Commands;

import Commands.Sub.Compare;
import Enums.Interval;
import DataObjects.NickInfo;

/**
 * Created by Hans on 26-07-2016.
 */
public class CompareStart extends AbstractCommand {

    private NickInfo nickInfo1 = new NickInfo();
    private NickInfo nickInfo2 = new NickInfo();

    public CompareStart(Interval interval) {
        this.interval = interval;
    }

    protected boolean handleParams(String[] params) {

        boolean isSelf = false;

        if (params.length > 1) {
            setNickInfo(params[0], nickInfo1, "lastfm");
            setNickInfo(params[1], nickInfo2, "lastfm");
        } else if (params.length == 1) {
            setNickInfo(sender, nickInfo1, "lastfm");
            setNickInfo(params[0], nickInfo2, "lastfm");
            isSelf = true;
        } else {
            return false;
        }

        if (isSelf && !nickInfo1.getIsReg()) {
            return false;
        }

        return true;
    }

    public String getOutput() {
        Compare compare = new Compare();

        return compare.getOutput(interval.getLfmInterval(), nickInfo1, nickInfo2);
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "COMPARE[interval]";
        String explain = "Compares artists of the given users based on their top 1000. " + info.getSplit();
        String intervals = info.getSplit() + " Intervals: Month, All. Default: Year";
        return explain + " Usage: " + cmd + " <IRC Nick|Last.fm Nick> <IRC Nick|Last.fm Nick> OR " + cmd + " <IRC Nick|Last.fm Nick> for registered irc nicks. " + intervals;
    }



}
