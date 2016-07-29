package Enums;

/**
 * Created by Hans on 27-07-2016.
 */
public enum Interval {
    ALL("overall"), YEAR("12month"), MONTH("1month"), WEEK("7day"), TODAY("24hour");

    private final String lfmInterval;
    Interval(String lfmInterval) {
        this.lfmInterval = lfmInterval;
    }

    public String getLfmInterval() {
        return lfmInterval;
    }
}
