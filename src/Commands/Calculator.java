package Commands;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import org.jibble.pircbot.Colors;

/**
 * Created by Hans on 28-07-2016.
 */
public class Calculator extends AbstractCommand {

    private String exp;

    protected boolean handleParams(String[] params) {

        if (params.length > 0) {
            exp = arrayToString(params, 0);
        } else {
            return false;
        }

        return true;
    }

    public String getHelp() {
        String cmd = info.getIdentifier() + "C";
        String explain = "A simple calculator for the lazy. " + info.getSplit();
        return explain + " Usage: " + cmd + " <Math Expression>";
    }

    public String getOutput() {
        Evaluator eval = new Evaluator();

        try {
            eval.putVariable("pi", "3.1415926535");
            eval.putVariable("e", "2.7182818284");

            double result = Double.parseDouble(eval.evaluate(exp));
            String ev;
            if (Math.round(result) == result) {
                ev = String.valueOf((int) result);
            } else {
                ev = String.valueOf(Math.round(result * 1000.0) / 1000.0);
            }
            return exp + " = " + Colors.BOLD + ev;

        } catch (EvaluationException|NumberFormatException e) {
            return "can't evaluate \"" + exp + "\"";
        }
    }
}
