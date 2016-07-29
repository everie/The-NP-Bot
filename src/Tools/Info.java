package Tools;

import DataObjects.BotInfo;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Hans on 25-07-2016.
 */
public class Info {
    private static BotInfo info;

    private static File settingsFile = new File("settings.txt");
    private static File channelsFile = new File("channels.txt");

    public static void getSettings() {
        BotInfo botInfo = new BotInfo();

        try {
            // Reading Settings

            Scanner scanner = new Scanner(settingsFile);

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("=");

                /*
                for (String s : line) {
                    System.out.println(s);
                }
                */


                if (line.length > 1) {

                    String variable = line[0];
                    String data = line[1];

                    switch (variable) {
                        case "ircNick":
                            botInfo.setIrcNick(data);
                            break;
                        case "ircLogin":
                            botInfo.setIrcLogin(data);
                            break;
                        case "ircName":
                            botInfo.setIrcName(data);
                            break;
                        case "ircServer":
                            botInfo.setIrcServer(data);
                            break;
                        case "ownerNick":
                            botInfo.setOwnerNick(data);
                            break;
                        case "ownerHost":
                            botInfo.setOwnerHost(data);
                            break;

                        case "dbHost":
                            botInfo.setDbHost(data);
                            break;
                        case "dbPort":
                            botInfo.setDbPort(data);
                            break;
                        case "dbName":
                            botInfo.setDbName(data);
                            break;
                        case "dbUser":
                            botInfo.setDbUser(data);
                            break;
                        case "dbPassword":
                            botInfo.setDbPassword(data);
                            break;

                        case "verbose":
                            botInfo.setVerbose(data);
                            break;
                        case "commandIdentifier":
                            botInfo.setIdentifier(data);
                            break;
                        case "splitSymbol":
                            botInfo.setSplit(data);
                            break;

                        case "apiLastFM":
                            botInfo.setApiLastFM(data);
                            break;
                        case "apiTraktTV":
                            botInfo.setApiTraktTV(data);
                            break;
                        case "apiGoogle":
                            botInfo.setApiGoogle(data);
                            break;
                        case "apiOpenWeatherMap":
                            botInfo.setApiOpenWeaherMap(data);
                            break;
                        case "apiUrbanDictionary":
                            botInfo.setApiUrbanDictionary(data);
                            break;
                        case "apiYandex":
                            botInfo.setApiYandex(data);
                            break;
                    }

                }
            }

            // Reading Channels

            scanner = new Scanner(channelsFile);

            while(scanner.hasNextLine()) {
                botInfo.getChannels().add(scanner.nextLine());
            }

            scanner.close();

            info = botInfo;

        } catch (IOException e) {
            System.out.println("Something went wrong, check your settings file");
            System.exit(0);
            //e.printStackTrace();
        }

    }

    public static void setInfo(BotInfo infoObj) {
        info = infoObj;
    }

    public static BotInfo getInfo() {
        return info;
    }
}
