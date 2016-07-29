package Commands;

import Commands.Sub.TopPlays;
import Commands.Sub.TopTags;
import DataObjects.NickInfo;
import Enums.Interval;

/**
 * Created by Hans on 26-07-2016.
 */
public class TagsStart extends AbstractCommand {

    private NickInfo nickInfo = new NickInfo();

    public TagsStart(Interval interval) {
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
        TopTags tags = new TopTags();

        return tags.getOutput(interval.getLfmInterval(), nickInfo);
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "TAGS[interval]";
        String intervals = info.getSplit() + " Intervals: Month, Year, All. Default: Week";
        String explain = "Displays a users top tags based on their top 50 artists of the given interval. " + info.getSplit();
        return explain + " Usage: " + cmd + " " + intervals;
    }
}
