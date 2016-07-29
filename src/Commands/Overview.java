package Commands;

import Bot.Command;
import Enums.Category;
import DataObjects.CommandInfo;
import org.jibble.pircbot.Colors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hans on 26-07-2016.
 */
public class Overview extends AbstractCommand {

    public String getOutput() {
        return getCommands();
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "HELP";
        String explain = "Displays a list of available commands. " + info.getSplit();
        return explain + " Usage: " + cmd;
    }

    protected boolean handleParams(String[] params) {
        return true;
    }

    private String getCommands() {
        Command cmd = new Command();

        Map<Category, ArrayList<CommandInfo>> commands = new HashMap<>();

        for (Map.Entry<String, CommandInfo> entry : cmd.getMap().entrySet()) {
            if (!entry.getKey().equals("HELP")) {
                CommandInfo ci = entry.getValue();
                ci.setIdentifier(entry.getKey());

                if (commands.containsKey(ci.getCategory())) {
                    commands.get(ci.getCategory()).add(ci);
                } else {
                    ArrayList<CommandInfo> tempList = new ArrayList<>();
                    tempList.add(ci);
                    commands.put(ci.getCategory(), tempList);
                }
            }
        }

        //System.out.println(commands);

        String display = "";

        for (Map.Entry<Category, ArrayList<CommandInfo>> entry : commands.entrySet()) {
            display += Colors.BOLD + entry.getKey().toString() + Colors.NORMAL + ": ";

            Map<String, ArrayList<CommandInfo>> parts = new HashMap<>();
            for (CommandInfo ci : entry.getValue()) {
                if (parts.containsKey(ci.getPartOf())) {
                    parts.get(ci.getPartOf()).add(ci);
                } else {
                    ArrayList<CommandInfo> tempList = new ArrayList<>();
                    tempList.add(ci);
                    parts.put(ci.getPartOf(), tempList);
                }
            }

            for (Map.Entry<String, ArrayList<CommandInfo>> part : parts.entrySet()) {

                if (part.getKey() != null) {
                    String dispCmd = info.getIdentifier() + part.getKey().toLowerCase() + "[";

                    int i = 0;
                    for (CommandInfo partCmd : part.getValue()) {
                        if (!partCmd.isDefault()) {
                            if (i > 0) {
                                dispCmd += "|";
                            }
                            dispCmd += partCmd.getCommand().getInterval().toString().toLowerCase();
                            i++;
                        }
                    }

                    dispCmd += "]";
                    display += dispCmd + " ";
                } else {
                    for (CommandInfo soloCmd : part.getValue()) {
                        display += info.getIdentifier() + soloCmd.getIdentifier().toLowerCase() + " ";
                    }
                }
            }

            //System.out.println(parts);
        }

        return display;
    }
}
