package Commands;

import Commands.Sub.Obscure;
import DataObjects.NickInfo;
import Enums.Interval;

/**
 * Created by Hans on 14-08-2016.
 */
public class ObscureStart extends AbstractCommand {

    private NickInfo nickInfo = new NickInfo();
    private String type;

    public ObscureStart(Interval interval, String type) {
        this.interval = interval;
        this.type = type;
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
        Obscure stats = new Obscure();

        return stats.getOutput(interval, type, nickInfo);
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "OBSCURE[interval]";
        String intervals = info.getSplit() + " Intervals: Week, Year, All. Default: Month";
        String explain = "Want to know how special you are? " + info.getSplit();
        return explain + " Usage: " + cmd + " <IRC Nick|Last.fm Nick> OR " + cmd + " alone for registered irc nicks. " + intervals;
    }



}
