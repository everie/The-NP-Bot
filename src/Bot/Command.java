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

    // new CommandInfo(new NowPlaying(), category, partOf, isDefault, display);

    public Command() {
        // REGISTRATION
        commandMap.put("LASTFM", new CommandInfo(new Registration("lastfm"), Category.Registration, null, true, true));
        commandMap.put("TRAKT", new CommandInfo(new Registration("trakt"), Category.Registration, null, true, true));
        commandMap.put("LOCATION", new CommandInfo(new Registration("location"), Category.Registration, null, true, true));
        commandMap.put("AUTH", new CommandInfo(new Authentication("token"), Category.Registration, null, true, false));
        commandMap.put("DONE", new CommandInfo(new Authentication("session"), Category.Registration, null, true, false));
        // SPAM
        commandMap.put("HELP", new CommandInfo(new Overview(), Category.Spam, null, true, false));
        commandMap.put("NP", new CommandInfo(new NowPlaying(), Category.Spam, null, true, true));
        commandMap.put("TV", new CommandInfo(new NowWatching(), Category.Spam, null, true, true));
        commandMap.put("WEATHER", new CommandInfo(new Weather(), Category.Spam, null, true, true));
        commandMap.put("FORECAST", new CommandInfo(new WeatherForecast(), Category.Spam, null, true, true));
        commandMap.put("EXCUSE", new CommandInfo(new Excuse(), Category.Spam, null, true, false));
        commandMap.put("ISITBEDTIME?", new CommandInfo(new Bedtime(), Category.Spam, null, true, false));
        // STATS
        commandMap.put("COMPARE", new CommandInfo(new CompareStart(Interval.YEAR), Category.Stats, "compare", true, true));
        commandMap.put("COMPAREMONTH", new CommandInfo(new CompareStart(Interval.MONTH), Category.Stats, "compare", false, true));
        commandMap.put("COMPAREALL", new CommandInfo(new CompareStart(Interval.ALL), Category.Stats, "compare", false, true));
        commandMap.put("STATS", new CommandInfo(new StatsStart(Interval.ALL), Category.Stats, "stats", true, true));
        commandMap.put("STATSWEEK", new CommandInfo(new StatsStart(Interval.WEEK), Category.Stats, "stats", false, true));
        commandMap.put("STATSMONTH", new CommandInfo(new StatsStart(Interval.MONTH), Category.Stats, "stats", false, true));
        commandMap.put("STATSYEAR", new CommandInfo(new StatsStart(Interval.YEAR), Category.Stats, "stats", false, true));
        commandMap.put("TOP", new CommandInfo(new TopStart(Interval.TODAY), Category.Stats, "top", true, true));
        commandMap.put("TOPWEEK", new CommandInfo(new TopStart(Interval.WEEK), Category.Stats, "top", false, true));
        commandMap.put("TOPMONTH", new CommandInfo(new TopStart(Interval.MONTH), Category.Stats, "top", false, true));
        commandMap.put("TOPYEAR", new CommandInfo(new TopStart(Interval.YEAR), Category.Stats, "top", false, true));
        commandMap.put("TOPALL", new CommandInfo(new TopStart(Interval.ALL), Category.Stats, "top", false, true));
        commandMap.put("ARTIST", new CommandInfo(new ArtistStats(), Category.Stats, null, true, true));
        commandMap.put("TOPARTIST", new CommandInfo(new ArtistTop(), Category.Stats, null, true, true));
        commandMap.put("RECENT", new CommandInfo(new RecentTop(), Category.Stats, null, true, true));
        commandMap.put("TAGS", new CommandInfo(new TagsStart(Interval.WEEK), Category.Stats, "tags", true, true));
        commandMap.put("TAGSMONTH", new CommandInfo(new TagsStart(Interval.MONTH), Category.Stats, "tags", false, true));
        commandMap.put("TAGSYEAR", new CommandInfo(new TagsStart(Interval.YEAR), Category.Stats, "tags", false, true));
        commandMap.put("TAGSALL", new CommandInfo(new TagsStart(Interval.ALL), Category.Stats, "tags", false, true));
        commandMap.put("TOPAVERAGE", new CommandInfo(new TopAverage(), Category.Stats, null, true, true));
        commandMap.put("OBSCURE", new CommandInfo(new ObscureStart(Interval.MONTH), Category.Stats, "obscure", true, true));
        commandMap.put("OBSCUREWEEK", new CommandInfo(new ObscureStart(Interval.WEEK), Category.Stats, "obscure", false, true));
        commandMap.put("OBSCUREYEAR", new CommandInfo(new ObscureStart(Interval.YEAR), Category.Stats, "obscure", false, true));
        commandMap.put("OBSCUREALL", new CommandInfo(new ObscureStart(Interval.ALL), Category.Stats, "obscure", false, true));
        // SEARCH
        commandMap.put("G", new CommandInfo(new SearchGoogle(), Category.Search, null, true, true));
        commandMap.put("SPOT", new CommandInfo(new SearchSpotify(), Category.Search, null, true, true));
        commandMap.put("YT", new CommandInfo(new SearchYoutube(), Category.Search, null, true, true));
        // OTHER
        commandMap.put("CUR", new CommandInfo(new Currency(), Category.Other, null, true, true));
        commandMap.put("C", new CommandInfo(new Calculator(), Category.Other, null, true, true));
        commandMap.put("UD", new CommandInfo(new UrbanDictionary(), Category.Other, null, true, true));
        commandMap.put("T", new CommandInfo(new Translate(), Category.Other, null, true, true));
        commandMap.put("SEEN", new CommandInfo(new LastSeen(), Category.Other, null, true, true));
        commandMap.put("SEEN24", new CommandInfo(new LastSeen24(), Category.Other, null, true, true));
        // LAST.FM
        commandMap.put("LOVE", new CommandInfo(new TrackLove(true), Category.LastFM, null, true, true));
        commandMap.put("UNLOVE", new CommandInfo(new TrackLove(false), Category.LastFM, null, true, true));
        commandMap.put("TAG", new CommandInfo(new TrackTag(), Category.LastFM, null, true, true));
        commandMap.put("TAGARTIST", new CommandInfo(new TrackArtistTag(), Category.LastFM, null, true, true));
    }

    public Map<String, CommandInfo> getMap() {
        return commandMap;
    }
}
