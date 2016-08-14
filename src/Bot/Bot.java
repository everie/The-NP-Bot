package Bot;

import DataObjects.BotInfo;
import DataObjects.Reply;
import Database.Seen;
import Tools.Info;
import Tools.Web;
import org.jibble.pircbot.PircBot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hans on 22-07-2016.
 */
public class Bot extends PircBot {

    private CommandFactory cf;
    private BotInfo info = Info.getInfo();
    private Seen seen = new Seen();
    private Web web = new Web();
    private Set<String> ignoreNicks = new HashSet<>(Arrays.asList("MrRoboto", "DrumsRadio", "BAIT", "FailBot"));

    private int splitLength = 300;

    public Bot() {
        this.setName(info.getIrcNick());
        this.setLogin(info.getIrcLogin());
        this.setVersion(info.getIrcName());

        this.cf = new CommandFactory(this);
    }

    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        if (sender.equals(info.getOwnerNick()) && hostname.equals(info.getOwnerHost())) {
            String channel = message.split(" ")[0];
            String output = message.substring(channel.length() + 1);
            sendMessage(channel, output);
        }
    }

    public void onMessage(String channel, String sender,
                          String login, String hostname, String message) {

        // COMMAND READER
        if (message.startsWith(info.getIdentifier())) {
            Reply r = cf.getOutput(message.split(" "), sender, channel);

            if (r != null) {
                String output = r.getMessage();
                if (output.length() > splitLength) {
                    String[] parts = output.split(info.getSplit());
                    String send = "";
                    for (int i = 0; i < parts.length; i++) {
                        String p = parts[i];
                        if (send.length() + p.length() > splitLength) {
                            sendReply(send, r.getType(), channel, sender);
                            send = p.substring(1);
                        } else {
                            if (i > 0) {
                                send += info.getSplit();
                            }
                            send += p;
                        }
                    }


                    if (send.length() > 0) {
                        sendReply(send, r.getType(), channel, sender);
                    }

                } else {
                    sendReply(output, r.getType(), channel, sender);
                }
            }


        // LINK READER
        } else if (message.contains("http://") || message.contains("https://")) {
            String[] msg = message.split(" ");

            if (!ignoreNicks.contains(sender)) {

                for (int i = 0; i < msg.length; i++) {
                    String link = msg[i];
                    if (link.startsWith("http://") || link.startsWith("https://")) {
                        String title = web.getTitle(msg[i], sender);
                        sendMessage(channel, title);
                    }
                }

            }
        }

        // KEEPING TRACK OF NICKS
        seen.setLastSeen(sender, message, channel);
    }

    public void sendReply(String output, String type, String channel, String sender) {
        switch (type) {
            case "message":
                sendMessage(channel, output);
                break;

            case "notice":
                sendNotice(sender, output);
                break;

            case "private":
                sendMessage(sender, output);
                break;

            default:
                sendMessage(channel, output);
                break;
        }
    }

}
