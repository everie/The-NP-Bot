package Bot;

import Commands.AbstractCommand;
import Commands.Authentication;
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
            boolean success = ac.instantiate(getParamters(params), sender, channel);

            if (success) {

                switch (command) {
                    case "HELP":
                        return new Reply("notice", ac.getOutput(), channel);

                    default:
                        return new Reply("message", ac.getOutput(), channel);
                }
            } else {
                String errorMessage = "Insufficient Parameters " + info.getSplit() + " type " + Colors.BOLD + info.getIdentifier() + "HELP " + command + Colors.NORMAL + " for more info.";
                return new Reply("message", errorMessage, channel);
            }

        } catch (NullPointerException e) {
            return null;
        }
    }

    public Reply getPrivateAuth(String[] params, String sender) {
        String command = params[0].substring(info.getIdentifier().length()).toUpperCase();

        try {
            Authentication ac = (Authentication) cmd.getMap().get(command).getCommand();
            ac.instantiate(getParamters(params), sender);

            Reply reply = new Reply();

            switch (command) {
                case "AUTH":
                    reply.setType("private");
                    reply.setMessage(Colors.BOLD + "Step 1: " + Colors.NORMAL + ac.getOutput());
                    reply.setMoreMessage(Colors.BOLD + "Step 2:" + Colors.NORMAL + " Once you've signed in and allowed access via the link above type " + Colors.BOLD + info.getIdentifier() + "DONE" + Colors.NORMAL);
                    return reply;

                default:
                    reply.setType("private");
                    reply.setMessage(ac.getOutput());
                    return reply;
            }

        } catch (NullPointerException e) {
            return null;
        }

    }

    private String[] getParamters(String[] input) {
        return Arrays.copyOfRange(input, 1, input.length);
    }

}
