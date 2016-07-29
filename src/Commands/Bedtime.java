package Commands;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Hans on 28-07-2016.
 */
public class Bedtime extends AbstractCommand {

    protected boolean handleParams(String[] params) {
        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "ISITBEDTIME?";
        String explain = "Relieves you of making the decision of whether it's bedtime or not. " + info.getSplit();
        return explain + " Usage: " + cmd;
    }

    private ArrayList<String> answers = new ArrayList<>(Arrays.asList(
            "Just go to bed already!",
            "FUCK YEAH!",
            "Why are you not in bed already?",
            "You should already be sleeping by now!",
            "GO TO BED! NO MORE STALLING!",
            "yes...",
            "Yes, yes it is and has been for quite some time..",
            "NO! You need to stay up all night!",
            "Naaah, why don't you check reddit for more cat pics?",
            "Sure, but look at all these people chatting, what if you miss something?",
            "You know, if you need to ask it probably is..",
            "I'm not your mom, I can't tell you when it's bedtime! ..But yes, yes it is!",
            "Why don't you stay up another hour just to be sure?",
            "WOOOOOH!!! BEDTIME!!! WOOOOOH!!",
            "It was bedtime 5 minutes ago, but you missed it :(",
            "Always asking the important questions, aren't you?",
            "lol, you're asking a bot for advice..",
            "Wait for it... waaaiit for iiiit, NOW it is!",
            "One would think it was, but it isn't.",
            "SLEEP ALL THE SLEEPS! DREAM ALL THE DREAMS!",
            "Don't let your dreams be dreams!",
            "It is, for everyone except you!",
            "Just one more track...",
            "Ask someone else, I'm tired of your shit!"
    ));

    private static ArrayList<Integer> order = new ArrayList<>();

    public String getOutput() {

        if (order.size() < 1) {
            for (int i = 0; i < answers.size(); i++) {
                order.add(i);
            }
            Collections.shuffle(order);
        }

        int orderNo = order.get(0);
        order.remove(0);
        return answers.get(orderNo);
    }
}
