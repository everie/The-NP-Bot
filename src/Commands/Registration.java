package Commands;

import Database.User;

/**
 * Created by Hans on 27-07-2016.
 */
public class Registration extends AbstractCommand {

    String type;
    String nick;

    public Registration(String type) {
        this.type = type;
    }

    protected boolean handleParams(String[] params) {

        if (params.length > 0) {
            nick = params[0];
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String input;

        switch (type) {
            case "lastfm":
                input = "last.fm username";
                break;
            case "trakt":
                input = "trakt.tv username";
                break;
            case "location":
                input = "your most common location";
                break;

            default:
                input = "last.fm username";
                break;
        }

        String cmd = info.getIdentifier() + type;
        String explain = "Lets you register your nick in the database. " + info.getSplit();
        return explain + " Usage: " + cmd + " <" + input + ">";
    }

    public String getOutput() {
        User user = new User();
        return user.registerUser(sender, nick, type);
    }
}
