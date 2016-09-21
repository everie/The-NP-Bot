package Enums;

/**
 * Created by Hans on 27-07-2016.
 */
public enum Interval {
    ALL("overall", "Overall"),
    YEAR("12month", "Last year"),
    MONTH("1month", "Last month"),
    WEEK("7day", "Last week"),
    TODAY("24hour", "Last 24 hours");

    private final String lfmInterval;
    private final String textInterval;
    Interval(String lfmInterval, String textInterval) {
        this.lfmInterval = lfmInterval;
        this.textInterval = textInterval;
    }

    public String getLfmInterval() {
        return lfmInterval;
    }

    public String getTextInterval() {
        return textInterval;
    }
}
