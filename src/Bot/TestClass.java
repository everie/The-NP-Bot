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


        AbstractCommand ac = commands.get("STATSWEEK").getCommand();
        ac.instantiate(new String[]{"Everie"}, "Psi", "#everieneverie", "lol.com");
        System.out.println(ac.getOutput());

        /*
        AbstractCommand ac = commands.get("EP").getCommand();
        ac.instantiate(new String[]{"arrested development"}, "Psi", "#everieneverie", "lol.com");
        System.out.println(ac.getOutput());
        */

        /*
        AbstractCommand ac = commands.get("ARTISTTRACKS").getCommand();
        ac.instantiate(new String[]{"Chemical Brothers"}, "Psi", "#everieneverie", "lol.com");
        System.out.println(ac.getOutput());
        */

        /*
        AbstractCommand ac = commands.get("MAINSTREAMALL").getCommand();
        ac.instantiate(new String[]{"MePH"}, "Psi", "#everieneverie", "lol.com");
        System.out.println(ac.getOutput());
        */

        /*
        AbstractCommand ac = commands.get("COMPARE").getCommand();
        ac.instantiate(new String[]{"Everie", "MePH"}, "MePH", "#everieneverie", "whatev");
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
