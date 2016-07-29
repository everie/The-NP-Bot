package Bot;

import Commands.AbstractCommand;
import DataObjects.BotInfo;
import DataObjects.Reply;
import Tools.Info;
import org.jibble.pircbot.Colors;

import java.util.Arrays;

/**
 * Created by Hans on 22-07-2016.
 */
public class CommandFactory {

    private BotInfo info = Info.getInfo();
    private Command cmd = new Command();

    public CommandFactory(Bot bot) {
        info.setBot(bot);
    }

    public Reply getOutput(String[] params, String sender, String channel) {

        String command = params[0].substring(info.getIdentifier().length()).toUpperCase();

        try {

            AbstractCommand ac = cmd.getMap().get(command).getCommand();

            if (params.length > 1 && params[1].toUpperCase().equals("HELP")) {
                return new Reply("notice", ac.getHelp(), channel);
            }

            boolean success = ac.instantiate(getParamters(params), sender, channel);
            if (success) {
                if (command.toUpperCase().equals("HELP")) {
                    return new Reply("notice", ac.getOutput(), channel);
                } else {
                    return new Reply("message", ac.getOutput(), channel);
                }
            } else {
                String errorMessage = "Insufficient Parameters " + info.getSplit() + " type " + Colors.BOLD + info.getIdentifier() + command + " help" + Colors.NORMAL + " for more info.";
                return new Reply("message", errorMessage, channel);
            }

        } catch (NullPointerException e) {
            return null;
        }
    }

    private String[] getParamters(String[] input) {
        return Arrays.copyOfRange(input, 1, input.length);
    }

}
