package Commands;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Hans on 28-07-2016.
 */
public class Currency extends AbstractCommand {

    String amount;
    String from;
    String to;

    protected boolean handleParams(String[] params) {
        if (params.length > 2) {
            amount = params[0];
            from = params[1];
            to = params[2];
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "CUR";
        String explain = "Lets you convert one currency into another. " + info.getSplit();
        return explain + " Usage: " + cmd + " <amount> <from> <to>";
    }

    public String getOutput() {

        String ret = "";

        String inFrom = from.toUpperCase();
        String inTo = to.toUpperCase();

        try
        {
            String input = toolBox.apiToString("https://currency-api.appspot.com/api/" + from + "/" + to + ".json");
            JSONObject obj = new JSONObject(input);
            Boolean success = obj.getBoolean("success");

            if (success) {

                Double cur = obj.getDouble("rate");

                Double result = Math.round(cur * Double.parseDouble(amount) * 100.0) / 100.0;

                ret = amount + " " + inFrom + " = " + result + " " + inTo;

            } else {
                ret = "Error: " + obj.getString("message");
            }

        } catch (IOException | JSONException e) {
            ret = "Something went wrong. " + e.getMessage();
        } catch (NumberFormatException e) {
            ret = "Error: " + amount + " is not a number.";
        }

        return ret;
    }
}
