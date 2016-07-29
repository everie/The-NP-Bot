package Bot;

import DataObjects.BotInfo;
import Tools.Info;

/**
 * Created by Hans on 22-07-2016.
 */
public class BotLauncher {

    public static void main(String[] args) throws Exception {

        Info.getSettings();
        BotInfo info = Info.getInfo();


        // Now start our bot up.
        Bot bot = new Bot();

        // Enable debugging output.
        bot.setVerbose(info.getVerbose());

        // Connect to the IRC server.
        bot.connect(info.getIrcServer());

        // Join channels
        for (String channel : info.getChannels()) {
            bot.joinChannel(channel);
        }


    }



}
