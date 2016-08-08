package Bot;

import Commands.*;
import Enums.Category;
import DataObjects.CommandInfo;
import Enums.Interval;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hans on 24-07-2016.
 */
public class Command {
    private Map<String, CommandInfo> commandMap = new HashMap<>();

    // new CommandInfo(new NowPlaying(), category, partOf, isDefault);

    public Command() {
        // REGISTRATION
        commandMap.put("LASTFM", new CommandInfo(new Registration("lastfm"), Category.Registration, null, true));
        commandMap.put("TRAKT", new CommandInfo(new Registration("trakt"), Category.Registration, null, true));
        commandMap.put("LOCATION", new CommandInfo(new Registration("location"), Category.Registration, null, true));
        // SPAM
        commandMap.put("HELP", new CommandInfo(new Overview(), Category.Spam, null, true));
        commandMap.put("NP", new CommandInfo(new NowPlaying(), Category.Spam, null, true));
        commandMap.put("TV", new CommandInfo(new NowWatching(), Category.Spam, null, true));
        commandMap.put("WEATHER", new CommandInfo(new Weather(), Category.Spam, null, true));
        commandMap.put("FORECAST", new CommandInfo(new WeatherForecast(), Category.Spam, null, true));
        commandMap.put("EXCUSE", new CommandInfo(new Excuse(), Category.Spam, null, true));
        commandMap.put("ISITBEDTIME?", new CommandInfo(new Bedtime(), Category.Spam, null, true));
        // STATS
        commandMap.put("COMPARE", new CommandInfo(new CompareStart(Interval.YEAR), Category.Stats, "compare", true));
        commandMap.put("COMPAREMONTH", new CommandInfo(new CompareStart(Interval.MONTH), Category.Stats, "compare", false));
        commandMap.put("COMPAREALL", new CommandInfo(new CompareStart(Interval.ALL), Category.Stats, "compare", false));
        commandMap.put("STATS", new CommandInfo(new StatsStart(Interval.ALL), Category.Stats, "stats", true));
        commandMap.put("STATSWEEK", new CommandInfo(new StatsStart(Interval.WEEK), Category.Stats, "stats", false));
        commandMap.put("STATSMONTH", new CommandInfo(new StatsStart(Interval.MONTH), Category.Stats, "stats", false));
        commandMap.put("STATSYEAR", new CommandInfo(new StatsStart(Interval.YEAR), Category.Stats, "stats", false));
        commandMap.put("TOP", new CommandInfo(new TopStart(Interval.TODAY), Category.Stats, "top", true));
        commandMap.put("TOPWEEK", new CommandInfo(new TopStart(Interval.WEEK), Category.Stats, "top", false));
        commandMap.put("TOPMONTH", new CommandInfo(new TopStart(Interval.MONTH), Category.Stats, "top", false));
        commandMap.put("TOPYEAR", new CommandInfo(new TopStart(Interval.YEAR), Category.Stats, "top", false));
        commandMap.put("TOPALL", new CommandInfo(new TopStart(Interval.ALL), Category.Stats, "top", false));
        commandMap.put("ARTIST", new CommandInfo(new ArtistStats(), Category.Stats, null, true));
        commandMap.put("TOPARTIST", new CommandInfo(new ArtistTop(), Category.Stats, null, true));
        commandMap.put("RECENT", new CommandInfo(new RecentTop(), Category.Stats, null, true));
        commandMap.put("TAGS", new CommandInfo(new TagsStart(Interval.WEEK), Category.Stats, "tags", true));
        commandMap.put("TAGSMONTH", new CommandInfo(new TagsStart(Interval.MONTH), Category.Stats, "tags", false));
        commandMap.put("TAGSYEAR", new CommandInfo(new TagsStart(Interval.YEAR), Category.Stats, "tags", false));
        commandMap.put("TAGSALL", new CommandInfo(new TagsStart(Interval.ALL), Category.Stats, "tags", false));
        commandMap.put("TOPAVERAGE", new CommandInfo(new TopAverage(), Category.Stats, null, true));
        // SEARCH
        commandMap.put("G", new CommandInfo(new SearchGoogle(), Category.Search, null, true));
        commandMap.put("SPOT", new CommandInfo(new SearchSpotify(), Category.Search, null, true));
        commandMap.put("YT", new CommandInfo(new SearchYoutube(), Category.Search, null, true));
        // OTHER
        commandMap.put("CUR", new CommandInfo(new Currency(), Category.Other, null, true));
        commandMap.put("C", new CommandInfo(new Calculator(), Category.Other, null, true));
        commandMap.put("UD", new CommandInfo(new UrbanDictionary(), Category.Other, null, true));
        commandMap.put("T", new CommandInfo(new Translate(), Category.Other, null, true));
        commandMap.put("SEEN", new CommandInfo(new LastSeen(), Category.Other, null, true));
        commandMap.put("SEEN24", new CommandInfo(new LastSeen24(), Category.Other, null, true));

    }

    public Map<String, CommandInfo> getMap() {
        return commandMap;
    }
}
