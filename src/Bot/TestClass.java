package Bot;

import Commands.AbstractCommand;
import DataObjects.CommandInfo;
import Tools.Info;
import Tools.Web;

import java.util.Map;

/**
 * Created by Hans on 23-07-2016.
 */
public class TestClass {

    public static void main(String[] args) {
        //BotLauncher bl = new BotLauncher();
        Info.getSettings();
        //BotInfo info = Info.getInfo();
        Command cmd = new Command();
        /*
        Map<String, Class<? extends AbstractCommand>> classes = new HashMap<>();
        classes.put("np", NowPlaying.class);

        System.out.println(classes);

        try {
            Class[] cArg = new Class[]{String[].class, String.class};

            AbstractCommand ac = classes.get("np").getDeclaredConstructor(cArg).newInstance(new String[]{"Everie"}, "Everie");

            System.out.println(ac.getOutput());

        } catch (InstantiationException|IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
            e.printStackTrace();
        }
        */



        Map<String, CommandInfo> commands = cmd.getMap();

        System.out.println("#NP Top Overall Scrobblers • Total: 1016549 • Z\u200Bnurre: 132012 (13%) • J\u200BulianaStein: 127442 (13%) • i\u200Bhre: 106353 (10%) • Y\u200Bakuzing: 83276 • t\u200Bripod: 76557 • A\u200Blluti: 68835 • E\u200Bsko: 63916 • A\u200BLTHiR: 56037 • M\u200BePH: 52869 • E\u200Bverie: 51739 • S\u200Bamination: 42369 • s\u200Bsmmdd: 42144 • H\u200Bump: 37687 • M\u200Berson: 26026 • B\u200Bober: 18184 • C\u200Bheese: 10937 • A\u200Bmyndele: 10045 • E\u200Brror451: 7408 • q\u200B66: 2529 â".length());

        /*
        AbstractCommand ac = commands.get("COMPARE").getCommand();
        ac.instantiate(new String[]{"Everie", "Esko"}, "MePH", "#everieneverie");
        System.out.println(ac.getOutput());
        */

        /*
        AbstractCommand ac = commands.get("HELP").getCommand();
        ac.instantiate(new String[]{}, "Everie", "#np");
        System.out.println(ac.getOutput());
        */
        /*
        ac = commands.get("TOP").getCommand();
        ac.instantiate(new String[]{}, "Everie", "#np");
        System.out.println(ac.getOutput());
        */

    }
}
